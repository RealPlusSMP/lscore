package xyz.realplussmp.lSCore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageUtil {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static Component get(String message) {
        return reset(mm.deserialize(message));
    }

    private static Component reset(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }
}
