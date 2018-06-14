package ru.ifmo.ab.db.wiki;

import lombok.*;

@Data
public class AccessRights {
    private String who;
    private boolean read;
    private boolean modify;
    private boolean admin;
}
