package com.fluffcord.app.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicate that the name of the element may change when updating
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.TYPE})
public @interface Unstable {
}
