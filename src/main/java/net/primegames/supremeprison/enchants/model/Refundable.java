package net.primegames.supremeprison.enchants.model;

public interface Refundable {

    boolean isRefundEnabled();

    int getRefundGuiSlot();

    double getRefundPercentage();
}
