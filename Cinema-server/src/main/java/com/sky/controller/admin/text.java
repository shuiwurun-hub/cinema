package com.sky.controller.admin;

public class text {
    public static void main(String[] args) {
        int today = 6; // 星期六

        long base = 20 % 7;
        long exp = 22;
        long mod = 7;

        long powerMod = 1;
        for (int i = 0; i < exp; i++) {
            powerMod = (powerMod * base) % mod;
        }

        long resultDay = (today + powerMod) % mod;
        if (resultDay == 0) {
            System.out.println("星期日");
        } else {
            System.out.println("星期" + resultDay);
        }
    }

    }
