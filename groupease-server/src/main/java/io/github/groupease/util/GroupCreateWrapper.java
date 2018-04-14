package io.github.groupease.util;

import javax.annotation.Nonnull;

/**
 * Helper structure used by the REST framework to encapsulate JSON POSTed to the group
 * create/update APIs
 */
public class GroupCreateWrapper {
    public Long id;
    public Long channel;

    // While the name field is required, marking it @Nonnull causes a NullPointerException
    // when it isn't supplied. Therefore, intentionally allowing it to be nullable so that
    // the more explicit exception can be thrown by the consuming service instead
    public String name;
    public String description;
}
