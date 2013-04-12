package ru.dark32.chat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

@SuppressWarnings("deprecation")
public class Chat implements Listener {

    private double RangeMain; // локальный чат
    private double RangeWhispering; // шёпот
    private double RangeShout; // крик
    private int globalId; // Id вещи для вещания глобально
    private int globalSubId;// метадата вещи для вещания глобально
    private int worldId;// Id вещи для вещания мирово
    private int worldSubId;// метадата вещи для вещания мирово
    private int randrolldef;//Бросок по умолчанию = 100;
    private boolean Displayname;//выводить ли префиксы всюду
    private int defchanse; // шанс
    private int minroll;
    protected static Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");// Цвет
    protected static Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");// магия
    protected static Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");//жирный
    protected static Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");//зачёркнутый
    protected static Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");//подчёркнутый
    protected static Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");//косой
    protected static Pattern chatResetPattern = Pattern.compile("(?i)&([R])");//сброс
    protected static Pattern nick = Pattern.compile("[@2](\\D[\\d\\w_]+)\\s(.+)");// Извлекаем ник
    protected static Pattern _space = Pattern.compile("^\\s+");// пробел?
    protected static Pattern _number = Pattern.compile("^\\*(\\d+)$");// число?
    private Random rand = new Random();
    private String luck = ChatColor.GREEN + "(удачно)" + ChatColor.LIGHT_PURPLE;
    private String unluck = ChatColor.RED + "(не удачно)" + ChatColor.LIGHT_PURPLE;

    public Chat(FileConfiguration config) {
        this.RangeMain = config.getDouble("Range.main", this.RangeMain);// радиус обычного чата
        this.RangeWhispering = config.getDouble("Range.whispering", this.RangeWhispering);// радиус шёпота
        this.RangeShout = config.getDouble("Range.shout", this.RangeShout);// радиус крика
        this.globalId = config.getInt("Id.globalId", this.globalId);// вещь для вечания в глобальный чат
        this.globalSubId = config.getInt("Id.globalSubId", this.globalSubId);// метадата
        this.worldId = config.getInt("Id.worldId", this.worldId);//вещь для вещания в мировой чат
        this.worldSubId = config.getInt("Id.worldSubId", this.worldSubId);// методата
        this.randrolldef = config.getInt("randrolldef", this.randrolldef);// бросок кубика
        this.Displayname = config.getBoolean("Prefix", this.Displayname);// префиксы
        this.defchanse = config.getInt("defchanse", this.defchanse);// шанс
        this.minroll = config.getInt("minroll", this.minroll);// минимальный бросок

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateDisplayName(event.getPlayer());//обновляем префиксы
        //	event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();//получаем игрока, вызвавшего событие
        String message = "%1$s: %2$s";//формат сообщения 
        String chatMessage = event.getMessage();//сообщение
        int ranged = 1; // Тип чата
		/*
         * 3 - глобальный
         * 2 - мировой
         * 1 - флаг дистанции
         * 
         * @ - ЛС
         * $ - глобальный чат
         * > - мировой чат
         * ! - крик
         * # - шепот
         */
        ItemStack inHand = player.getItemInHand();//dtom d herf[
        double range = RangeMain;// локальный чат, радиус по умолчанию
        boolean isLocal = (chatMessage.startsWith("-")) && chatMessage.length() > 1;
        boolean isGlobal = !isLocal && (chatMessage.startsWith("$") || chatMessage.startsWith(";")) && chatMessage.length() > 1;
        boolean isWorld = !isLocal && (chatMessage.startsWith(">") || chatMessage.startsWith(".")) && chatMessage.length() > 1;
        boolean isShout = !isLocal && chatMessage.startsWith("!") && chatMessage.length() > 1;
        boolean isWhisper = !isLocal && (chatMessage.startsWith("#") || chatMessage.startsWith("№")) && chatMessage.length() > 1;
        boolean isPm = (chatMessage.startsWith("@") || chatMessage.startsWith("2")) && chatMessage.length() > 1;
        boolean isAny = isGlobal || isWorld || isShout || isWhisper;
        boolean isGlobalMode = !isLocal && main.getChatMode(player) == main.ChatMode.GLOBAL;
        boolean isWorldMode = !isLocal && !isGlobal && main.getChatMode(player) == main.ChatMode.WORLD;
        boolean isShoutMode = !isLocal && !isGlobal && !isWorld && main.getChatMode(player) == main.ChatMode.SHOUT;
        boolean isWhisperMode = !isLocal && !isGlobal && !isWorld && !isShout && main.getChatMode(player) == main.ChatMode.WHISPER;
        boolean isLocalMode = main.getChatMode(player) == main.ChatMode.LOCAL;

        // Глобальный
        if (isGlobal || isGlobalMode) {

            if (main.hasPermission(player, "mcnw.global")) {
                message = "%1$s всем: " + ChatColor.GOLD + "%2$s";
                range = RangeWhispering;
                if (main.hasPermission(player, "mcnw.global.no_item")) {
                    ranged = 3; // флаг глобально, межмирового чата
                } else if (inHand != null && inHand.getTypeId() == globalId && inHand.getDurability() == globalSubId) {
                    ranged = 3; // флаг глобально, межмирового чата
                    loseitem(player);
                    // message = "%1$s всем: " + ChatColor.GOLD + "%2$s";
                } else {
                    player.sendMessage(ChatColor.GRAY + "У вас нет нужного предмета");
                    event.setCancelled(true);
                }
            } else {
                player.sendMessage(ChatColor.GRAY + "У вас нет прав писать в глобальный чат");
                event.setCancelled(true);
            }
        }
        // Мировой

        if (isWorld || isWorldMode) {
            if (main.hasPermission(player, "mcnw.world")) {
                message = "%1$s в мир: " + ChatColor.GOLD + "%2$s";
                range = RangeWhispering;
                if (main.hasPermission(player, "mcnw.world.no_item")) {
                    ranged = 2;// флаг мирового чата
                } else if (inHand != null && inHand.getTypeId() == worldId && inHand.getDurability() == worldSubId) {
                    ranged = 2;// флаг мирового чата
                    loseitem(player);
                } else {
                    player.sendMessage(ChatColor.GRAY + "У вас нет нужного предмета");
                    event.setCancelled(true);
                }
            } else {
                player.sendMessage(ChatColor.GRAY + "У вас нет прав писать в мир");
                event.setCancelled(true);
            }
        }
        // Крик
        if (isShout || isShoutMode) {
            ranged = 1; // флаг дистанции
            range = RangeShout;// 1000;
            message = "%1$s кричит: " + ChatColor.RED + "%2$s";

        }
        // Шепот
        if (isWhisper || isWhisperMode) {
            ranged = 1;// флаг дистанции
            range = RangeWhispering;// 10;
            message = ChatColor.GRAY + "%1$s" + ChatColor.GRAY + " шепчет: %2$s";
        }
        // Личка
        if (isPm) {

            Matcher m = nick.matcher(chatMessage);
            if (m.find()) {
                ranged = -1;// флаг особого чата
                if (Bukkit.getServer().getPlayer(m.group(1)) != null) {
                    Player recipient = Bukkit.getServer().getPlayer(m.group(1));
                    if (!recipient.equals(player)) {
                        recipient.sendMessage(ChatColor.GRAY + "@" + name(player) + ": "
                                + ChatColor.DARK_PURPLE + m.group(2));
                    }
                    player.sendMessage(ChatColor.GRAY + "@" + name(player) + "->"
                            + name(recipient) + ": " + ChatColor.DARK_PURPLE + m.group(2));
                    if (!main.hasPermission(player, "mcnw.nospy")) {
                        getPMRecipientsSpy(player, recipient, m.group(2));
                    }

                    Bukkit.getConsoleSender().sendMessage(
                            ChatColor.GRAY + "spy@" + name(player) + "->" + name(recipient)
                            + ": " + m.group(2));
                } else {
                    player.sendMessage("Игрока " + m.group(1) + " нет в сети");
                }
                event.setCancelled(true);
            }
        }

        // Действие с вероятностью
        if (chatMessage.startsWith("*") && chatMessage.length() > 1) {
            ranged = 1;
            range = RangeMain;
            Matcher m = _number.matcher(chatMessage);
            int i;
            if (m.find()) {
                i = Integer.parseInt(m.group(1));
                i = i > minroll ? i : randrolldef;
                int j = rand.nextInt(i) + 1;
                message = ChatColor.LIGHT_PURPLE + "*" + name(player) + ChatColor.LIGHT_PURPLE + " выбрасывает " + j + " из " + i + "*";

            } else {
                chatMessage = ChatColor.LIGHT_PURPLE + (chatMessage.substring(1));
                int chance = rand.nextInt(100);
                message = ChatColor.LIGHT_PURPLE + "* " + name(player) + " " + chatMessage + ((chance > defchanse) ? luck : unluck) + " *";


            }
            //  }

        }

        if (ranged > 0 && chatMessage.length() > 1 && (isAny)) {
            if (range != RangeMain) {
                Matcher space = _space.matcher(chatMessage.substring(1));
                chatMessage = space.replaceFirst("");
            }
        }
        if (isLocal) {
            Matcher space = _space.matcher(chatMessage.substring(1));
            chatMessage = space.replaceFirst("");

        }
        if (chatMessage.startsWith("?") && chatMessage.length() == 1) {
            player.sendMessage(ChatColor.AQUA + "=============================================");
            player.sendMessage(ChatColor.GOLD + "RPchat v 0.78");
            player.sendMessage(ChatColor.GOLD + "Авторы: ufatos, dark32");
            player.sendMessage(ChatColor.GOLD + "http://bit.ly/12Q8z4q");
            player.sendMessage(ChatColor.GOLD + "?/. для справки режимов чата");
            player.sendMessage(ChatColor.GOLD + "?/? для справки по переключению режимов чата");
            player.sendMessage(ChatColor.AQUA + "=============================================");
            event.setCancelled(true);
        }
        if (chatMessage.startsWith("?/.") && chatMessage.length() == 3) {
            player.sendMessage(ChatColor.AQUA + "=============================================");
            player.sendMessage(ChatColor.GOLD + "$ или ; для глобального чата");
            player.sendMessage(ChatColor.GOLD + "> или . для мирового чата");
            player.sendMessage(ChatColor.GOLD + "! для крика");
            player.sendMessage(ChatColor.GOLD + "# или № для шепота");
            player.sendMessage(ChatColor.GOLD + "2|@<имя> сообщени для личного чата");
            player.sendMessage(ChatColor.GOLD + "*<действие> для вероятного действия или *<целое число больше " + minroll + "> для выброса случайного числа");

            player.sendMessage(ChatColor.AQUA + "=============================================");
            event.setCancelled(true);
        }
        if (chatMessage.startsWith("?/?") && chatMessage.length() == 3) {
            // setChatMode(player, ChatMode.GLOBAL);
            player.sendMessage(ChatColor.AQUA + "=============================================");
            player.sendMessage(ChatColor.GOLD + "?/g - для выбора глобального режима");
            player.sendMessage(ChatColor.GOLD + "?/w - для выбора мирового мирового");
            player.sendMessage(ChatColor.GOLD + "?/s - для выбора режима крика");
            player.sendMessage(ChatColor.GOLD + "?/v - для выбора режима шепота");
            player.sendMessage(ChatColor.GOLD + "?/l - для выбора локального режима");
            player.sendMessage(ChatColor.GOLD + "?/? - для вызоа этой справки");
            player.sendMessage(ChatColor.AQUA + "=============================================");
            event.setCancelled(true);
        }
        // режимы чата
        if (chatMessage.startsWith("?/g") && chatMessage.length() == 3) {
            main.setChatMode(player, main.ChatMode.GLOBAL);
            player.sendMessage(ChatColor.GOLD + "Режим изменён на глобальный");
            event.setCancelled(true);
        }
        if (chatMessage.startsWith("?/w") && chatMessage.length() == 3) {
            main.setChatMode(player, main.ChatMode.WORLD);
            player.sendMessage(ChatColor.GOLD + "Режим изменён на мировой");
            event.setCancelled(true);
        }
        if (chatMessage.startsWith("?/s") && chatMessage.length() == 3) {
            main.setChatMode(player, main.ChatMode.SHOUT);
            player.sendMessage(ChatColor.GOLD + "Режим изменён на крик");
            event.setCancelled(true);
        }
        if (chatMessage.startsWith("?/v") && chatMessage.length() == 3) {
            main.setChatMode(player, main.ChatMode.WHISPER);
            player.sendMessage(ChatColor.GOLD + "Режим изменён на шёпот");
            event.setCancelled(true);
        }
        if (chatMessage.startsWith("?/l") && chatMessage.length() == 3) {
            main.setChatMode(player, main.ChatMode.LOCAL);
            player.sendMessage(ChatColor.GOLD + "Режим изменён на локальный");
            event.setCancelled(true);
        }
        switch (ranged) {
            case 0:
                player.sendMessage("Тебя слышат все!!!");/* Не используется */

            case 1: {// если стоит флаг дистанции
                event.getRecipients().clear();
                event.getRecipients().addAll(this.getLocalRecipients(player, range));

                break;
            }
            case 2: {// если стоит флаг внутримирового чата
                event.getRecipients().clear();
                event.getRecipients().addAll(this.getWorldRecipients(player, message));
                break;
            }
            case 3: {// если стоит флаг межмирового чата
                break;
            }
        }
        event.setFormat(message);
        event.setMessage(chatMessage);
    }

    //вывод имени
    private String name(Player p) {
        return Displayname ? p.getDisplayName() : p.getName();

    }
    // теряем вещь

    private void loseitem(Player player) {
        ItemStack inHand = player.getItemInHand();
        int AmoutHand = inHand.getAmount() - 1;
        ItemStack inHandrem = new ItemStack(inHand.getTypeId(), inHand.getAmount() - 1);
        if (AmoutHand <= 0) {
            inHandrem = null;
        }
        player.setItemInHand(inHandrem);
        return;
    }

    // Лакальный чат
    protected List<Player> getLocalRecipients(Player sender, double range) {
        Location playerLocation = sender.getLocation();
        List<Player> recipients = new LinkedList<Player>();
        double squaredDistance = Math.pow(range, 2);
        // sender.sendMessage("567");
        for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
            if (recipient.getWorld().equals(sender.getWorld())
                    && playerLocation.distanceSquared(recipient.getLocation()) < squaredDistance) {
                recipients.add(recipient);
            } else if (main.hasPermission(recipient, "mcnw.spy")) {
                recipients.add(recipient);
            } else {
                continue;
            }
        }
        return recipients;
    }

    // Список тех кому отправлять(мир)
    protected List<Player> getWorldRecipients(Player sender, String message) {
        List<Player> recipients = new LinkedList<Player>();
        for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
            if (!recipient.getWorld().equals(sender.getWorld())) {
                continue;
            }
            recipients.add(recipient);
        }
        return recipients;
    }

    // Прослушка ЛС
    protected void getPMRecipientsSpy(Player sender, Player target, String msg) {
        for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
            if (main.hasPermission(recipient, "mcnw.pmspy") && !recipient.equals(target)) {
                recipient.sendMessage(ChatColor.GRAY + "spy@" + name(sender) + "->"
                        + name(target) + ": " + msg);
            }
        }
    }

    // ChatManager Permissions Prefix своровал код, чтобы не парится
    protected static String translateColorCodes(String string) {
        if (string == null) {
            return "";
        }
        String newstring = string;
        newstring = chatColorPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
        newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
        return newstring;
    }

    static void updateDisplayName(Player player) {
        if (!main.usePEX) {
            return;
        }
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player);
        if (user == null) {
            return;
        }
        player.setDisplayName(Chat.translateColorCodes(user.getPrefix() + player.getName() + user.getSuffix()));
    }
}
