package com.example.demo;
/**
 * Author: Ravi
 * Represents a bank account entity.
 * This class encapsulates information about a user's bank account,
 * including account number, user ID, bank ID, and routing number.
 */
public class BankAccount {
    private int accountNo;  // The account number associated with the bank account.
    private int userId;     // The ID of the user to whom the bank account belongs.
    private int bankId;     // The ID of the bank where the account is held.
    private int routingNo;  // The routing number for the bank account.

    /**
     * Constructs a new BankAccount with the specified details.
     *
     * @param accountNo The account number.
     * @param userId    The user ID.
     * @param bankId    The bank ID.
     * @param routingNo The routing number.
     */
    public BankAccount(int accountNo, int userId, int bankId, int routingNo) {
        this.accountNo = accountNo;
        this.userId = userId;
        this.bankId = bankId;
        this.routingNo = routingNo;
    }

    /**
     * Gets the account number.
     *
     * @return The account number.
     */
    public int getAccountNo() {
        return accountNo;
    }
    /**
     * Sets the account number.
     *
     * @param accountNo The account number to set.
     */
    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }
    /**
     * Gets the user ID.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Sets the user ID.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Gets the bank ID.
     *
     * @return The bank ID.
     */
    public int getBankId() {
        return bankId;
    }
    /**
     * Sets the bank ID.
     *
     * @param bankId The bank ID to set.
     */
    public void setBankId(int bankId) {
        this.bankId = bankId;
    }
    /**
     * Gets the routing number.
     *
     * @return The routing number.
     */
    public int getRoutingNo() {
        return routingNo;
    }
    /**
     * Sets the routing number.
     *
     * @param routingNo The routing number to set.
     */
    public void setRoutingNo(int routingNo) {
        this.routingNo = routingNo;
    }
}
