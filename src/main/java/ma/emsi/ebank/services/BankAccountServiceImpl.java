package ma.emsi.ebank.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import ma.emsi.ebank.entities.BankAccount;
import ma.emsi.ebank.entities.CurrentAccount;
import ma.emsi.ebank.entities.Customer;
import ma.emsi.ebank.entities.SavingsAccount;
import ma.emsi.ebank.repositories.AccountOperationRepository;
import ma.emsi.ebank.repositories.BankAccountRepository;
import ma.emsi.ebank.repositories.CustomerRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
@Log4j           //faire la journalisation avec API slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

   //Logger log= LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public Customer saveCustomer(Customer customer) {
        //log.info("saving customer"); logs message
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount saveBankAccount = bankAccountRepository.save(currentAccount)
        return saveBankAccount;
    }

    @Override
    public BankAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setId(UUID.randomUUID().toString());
        savingsAccount.setCreatedAt(new Date());
        savingsAccount.setBalance(initialBalance);
        savingsAccount.setCustomer(customer);
        savingsAccount.setInterestRate(interestRate);
        SavingsAccount saveBankAccount = bankAccountRepository.save(savingsAccount);
        return saveBankAccount;
    }
    

    @Override
    public List<Customer> customerslist() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow();

        return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) {

    }

    @Override
    public void credit(String accountId, double amount, String description) {

    }

    @Override
    public void transfer(String accountIdSrc, String accountIdDst, double amount) {

    }
}
