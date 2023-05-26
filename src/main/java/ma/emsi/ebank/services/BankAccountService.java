package ma.emsi.ebank.services;
import ma.emsi.ebank.entities.Customer;
import ma.emsi.ebank.entities.BankAccount;

import java.util.List;

//je peux creer client, compte, operation debit, credit, virement et consulter compte
public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId);
    BankAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId);
    List<Customer> customerslist();
    BankAccount getBankAccount(String accountId);
    void debit(String accountId, double amount, String description);
    void credit(String accountId, double amount, String description);
    void transfer(String accountIdSrc, String accountIdDst, double amount);

}
