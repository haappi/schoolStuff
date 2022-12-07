package io.github.haappi.BoardGame;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utils {

    public static JedisPool initJedis(HashMap<String, String> config) {
        if (!config.containsKey("USER")
                || !config.containsKey("PASS")
                || !config.containsKey("IP")
                || !config.containsKey("PORT")) {
            return new JedisPool();
        }
        return new JedisPool(
                String.format(
                        "redis://%s:%s@%s:%s",
                        config.get("USER"),
                        config.get("PASS"),
                        config.get("IP"),
                        config.get("PORT")));
    }

    /**
     * Loads a configuration file (or any file seperated with <b><font color ="aqua">=</font></b>)
     * This will create a new file if it does not exist, and generate a random {@link UUID} to write.
     * @param fileName The filename to load from.
     * @return A {@link HashMap} with the keys and values casted as {@link String}'s
     * @throws IOException If an IO error occurred.
     */
    public static HashMap<String, String> loadConfig(String fileName) throws IOException {
        createFile(fileName, true);
        ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get("config.txt")));
        HashMap<String, String> config = new HashMap<>();
        for (String line : lines) {
            String[] split = line.split("=");
            config.put(split[0], split[1]);
        }
        return config;
    }

    /**
     * Creates a file for you at the given {@link String} path.
     * @param path A {@link String} containing the path to create the file.
     * @param writeClient A {@link Boolean} stating whether to write a CLIENT-ID field or not.
     * @throws IOException Thrown if IO errors have occurred.
     */
    public static void createFile(String path, boolean writeClient) throws IOException {
        Path path1 = Paths.get(path);
        if (!Files.exists(path1)) {
            Files.createFile(path1);
            if (writeClient) {
                byte[] bytesData = ("CLIENT-ID=" + UUID.randomUUID()).getBytes();
                Files.write(path1, bytesData);
            }
        }
    }

    public static <T> JedisPubSub getListener() {
        String clientID = HelloApplication.getInstance().getClientID();
        return new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (message.contains("\"ignoreSelf\":true") && message.contains(clientID)) {
                    return; // Ignore messages sent by the same client.
                }
                T object = getObject(message);
                System.out.println("received " + message + " from " + channel);
                if (object instanceof ConnectedUser connectedUser) {
                    Lobby.addUserToConnected(connectedUser);
                    Utils.p(new ConnectedUser(HelloApplication.getInstance().getClientID(), HelloApplication.getInstance().getName()));
                    // if we're in the lobby view, add it to the ListView, else drop it.
                    // todo                   Utils.p(); // send a message so the newly connected
                    // user can get the current players of the game.
                }
            }
        };
    }

    /**
     * Attempts to cast a given {@link String} automatically into its {@link Class<T>}
     * <br>May return <b><font color ="orange">null</font></b> if the {@link ClassTypes} isn't found.
     *
     * @param json The {@link String} json of the given object.
     * @return <b><font color ="orange">null</font></b> or the {@link ClassTypes} casted properly.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(String json) {
        Map<Object, Object> map =
                HelloApplication.getInstance().getGson().fromJson(json, Map.class);
        ClassTypes constant;
        try {
            constant = ClassTypes.valueOf((String) map.get("classType"));
        } catch (IllegalArgumentException e) {
            return null;
        }
        switch (constant) {
            case TEST:
                return (T) HelloApplication.getInstance().getGson().fromJson(json, Test.class);
            case CONNECTED_USER:
                return (T)
                        HelloApplication.getInstance()
                                .getGson()
                                .fromJson(json, ConnectedUser.class);
            default:
                return null;
        }
    }

    /**
     * Casts the given {@link String} JSON into the provided {@link Class<T>}.
     * @param json A {@link String} containing the JSON received from the server.
     * @param tClass The {@link Class<T>} attempting to be casted into.
     * @return {@link T} The JSON String casted into the Class.
     */
    public static <T> T castType(String json, Class<T> tClass) {
        return HelloApplication.getInstance().getGson().fromJson(json, tClass);
    }

    /**
     * Returns a {@link FileInputStream} for a given asset in the assets' directory.
     * @param fileName A {@link String} containing the filename.
     * @return The {@link FileInputStream} if the file is found, else <b><font color ="orange">null</font></b>
     */
    public static FileInputStream getImage(String fileName) {
        fileName =
                fileName.replace(" ", "_").split("\\.")[0]; // Escape the dot, get the first element
        try {
            return new FileInputStream("src/main/resources/assets/" + fileName + ".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Publishes a class to Jedis.
     *
     * @param instance {@link Jedis} instance to use.
     * @param channel  A {@link String} specifying the channel name.
     * @param message  A JSON {@link String} with the information needed to pass. <br><font color="orange"><b>This must be valid JSON</font></b>
     */
    public static void p(Jedis instance, String channel, String message) {
        String newMessage = message.substring(0, message.length() - 1);
        newMessage =
                newMessage
                        + ",\"clientID\":\""
                        + HelloApplication.getInstance().getClientID()
                        + "\"}";
        // ,"clientID":"...";
        instance.publish(channel, newMessage);
    }

    /**
     * Automatically publishes a class to Jedis for you.
     * This method automatically handles getting and closing a resource.
     * The default channel specified in HelloController is used.
     *
     * @param object The object to publish.
     */
    public static void p(Object object) {
        HelloApplication instance = HelloApplication.getInstance();
        Jedis resource = instance.getResource();
        Utils.p(resource, instance.getLobbyCode(), instance.getGson().toJson(object));
        instance.returnResource(resource);
    }

    /**
     * Automatically publishes a JSON String to Jedis for you.
     * This method automatically handles getting and closing a resource.
     * The default channel specified in HelloController is used.
     *
     * @param object The JSON String to publish.
     */
    public static void p(String object) {
        HelloApplication instance = HelloApplication.getInstance();
        Jedis resource = instance.getResource();
        Utils.p(resource, instance.getLobbyCode(), object);
        instance.returnResource(resource);
    }

    /**
     * Retrieves the amount of connected clients to the specified channel.
     * This automatically handles getting and closing a {@link Jedis} resource.
     *
     * @param channel A {@link String} containing the channel to check for.
     * @return A {@link Long} containing the number of connected clients.
     */
    public static Long getNumCount(String channel) {
        Jedis resource = HelloApplication.getInstance().getResource();
        Map<String, Long> longHashMap = resource.pubsubNumSub(channel);

        return longHashMap.get(channel);
    }

    /**
     * Gets the current ping to the connected {@link JedisPool} instance as a {@link Long}.
     */
    public static Long getPing() {
        long start = System.currentTimeMillis();
        Jedis resource = HelloApplication.getInstance().getResource();
        resource.ping();
        HelloApplication.getInstance().returnResource(resource);
        return System.currentTimeMillis() - start;
    }
}
