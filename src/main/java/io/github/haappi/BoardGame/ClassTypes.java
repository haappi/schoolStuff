package io.github.haappi.BoardGame;

public enum ClassTypes {
    // https://stackoverflow.com/questions/3978654/best-way-to-create-enum-of-strings
    TEST("TEST"),
    UNKNOWN("UNKNOWN"),
    USER_LEFT("USER_LEFT"),
    CONNECTED_USER("CONNECTED_USER"),
    PLAYER_UNREADY_READY("PLAYER_UNREADY_READY"),
    MOVE_ELEMENT("MOVE_ELEMENT"),
    ROLE_PICKED("ROLE_PICKED"),
    NEW_PLAYER_JOIN("NEW_PLAYER_JOIN"),
    PING_REQUEST("PING_REQUEST"),
    START_GAME("START_GAME"),
    LOSE_SCREEN("LOSE_SCREEN"),
    INFORMATION_MESSAGE("INFORMATION_MESSAGE"),
    GAME_WON("GAME_WON"),
    GAME_LOST("GAME_LOST");

    private final String text;

    ClassTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text.toUpperCase();
    }
}
