package com.cldfire.xenforonotifier.model;

public enum ForumType {
    XENFORO("XenForo");

    private final String name;

    ForumType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ForumType fromName(String name) {
        for (ForumType forumType : values()) {
            if (name.equals(forumType.getName())) {
                return forumType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
