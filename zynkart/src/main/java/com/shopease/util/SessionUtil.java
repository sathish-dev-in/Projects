package com.shopease.util;

import com.shopease.model.User;

import javax.servlet.http.HttpSession;

public final class SessionUtil {

    private static final String USER_KEY = "loggedInUser";
    private static final String CART_COUNT_KEY = "cartCount";

    private SessionUtil() {}

    public static User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute(USER_KEY);
    }

    public static void setLoggedInUser(HttpSession session, User user) {
        session.setAttribute(USER_KEY, user);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(USER_KEY) != null;
    }

    public static boolean isAdmin(HttpSession session) {
        User user = getLoggedInUser(session);
        return user != null && user.isAdmin();
    }

    public static void setCartCount(HttpSession session, int count) {
        session.setAttribute(CART_COUNT_KEY, count);
    }

    public static int getCartCount(HttpSession session) {
        Object count = session.getAttribute(CART_COUNT_KEY);
        return count != null ? (int) count : 0;
    }

    public static void invalidate(HttpSession session) {
        session.invalidate();
    }
}
