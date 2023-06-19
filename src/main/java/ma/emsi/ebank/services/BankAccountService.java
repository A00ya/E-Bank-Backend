package ma.emsi.ebank.services;
import ma.emsi.ebank.dtos.*;
import ma.emsi.ebank.entities.Customer;
import ma.emsi.ebank.entities.BankAccount;
import ma.emsi.ebank.exceptions.BalanceNotSufficientExcepetion;
import ma.emsi.ebank.exceptions.BankAccountNotFoundException;
import ma.emsi.ebank.exceptions.CustomerNotFoundException;

import java.util.List;

//je peux creer client, compte, operation debit, credit, virement et consulter compte
public interface BankAccountService {
    Customer saveCustomer(Customer customer);

    //Logger log= LoggerFactory.getLogger(this.getClass().getName());
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId);
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId);
    List<CustomerDTO> customerslist();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BalanceNotSufficientExcepetion, BankAccountNotFoundException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSrc, String accountIdDst, double amount) throws BankAccountNotFoundException, BalanceNotSufficientExcepetion;

    List<BankAccountDTO> listBankAccount();

    CustomerDTO getCustomer(long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(long id);

    List<AccountOperationDTO> accountHistory(String id);

    AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException;
}
