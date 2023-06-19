package ma.emsi.ebank.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import ma.emsi.ebank.dtos.*;
import ma.emsi.ebank.entities.*;
import ma.emsi.ebank.enums.OperationType;
import ma.emsi.ebank.exceptions.BalanceNotSufficientExcepetion;
import ma.emsi.ebank.exceptions.BankAccountNotFoundException;
import ma.emsi.ebank.exceptions.CustomerNotFoundException;
import ma.emsi.ebank.mappers.BankAccountMapperImpl;
import ma.emsi.ebank.repositories.AccountOperationRepository;
import ma.emsi.ebank.repositories.BankAccountRepository;
import ma.emsi.ebank.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Log4j           //faire la journalisation avec API slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public Customer saveCustomer(Customer customer) {
        return null;
    }

    //Logger log= LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        //log.info("saving customer"); logs message
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount saveBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(saveBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setId(UUID.randomUUID().toString());
        savingsAccount.setCreatedAt(new Date());
        savingsAccount.setBalance(initialBalance);
        savingsAccount.setCustomer(customer);
        savingsAccount.setInterestRate(interestRate);
        SavingsAccount saveBankAccount = bankAccountRepository.save(savingsAccount);
        return dtoMapper.fromSavingBankAccount(saveBankAccount);
    }


    @Override
    public List<CustomerDTO> customerslist() {

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(cust -> dtoMapper.fromCustomer(cust))
                .toList();

        return customerDTOS;
    }



    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bank account not found"));
        if(bankAccount instanceof SavingsAccount){
            SavingsAccount savingsAccount = (SavingsAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingsAccount);
        }else{
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientExcepetion, BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bank account not found"));

        if(bankAccount.getBalance()<amount)
        throw new BalanceNotSufficientExcepetion("Balance not sufficent");
    AccountOperation accountOperation=new AccountOperation();
    accountOperation.setType(OperationType.DEBIT);
    accountOperation.setAmount(amount);
    accountOperation.setDescription(description);
    accountOperation.setOpDate(new Date());
    accountOperation.setBankAccount(bankAccount);
    accountOperationRepository.save(accountOperation);
    bankAccount.setBalance(bankAccount.getBalance()-amount);
    bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("bank account not found"));
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOpDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSrc, String accountIdDst, double amount) throws BankAccountNotFoundException, BalanceNotSufficientExcepetion {
        debit(accountIdSrc,amount,"Transfer to "+accountIdDst);
        credit(accountIdDst,amount,"Transfer from "+accountIdSrc);
    }
    @Override
    public List<BankAccountDTO> listBankAccount(){
       List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingsAccount) {
                SavingsAccount savingsAccount = (SavingsAccount) bankAccount;
                return dtoMapper.fromSavingBankAccount(savingsAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);

            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(long customerId) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        //log.info("saving customer"); logs message
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(long id){
        customerRepository.deleteById(id);
    }
    @Override
    public List<AccountOperationDTO> accountHistory(String id){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(id);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }
    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(id).orElse(null);
        if(bankAccount == null) throw new BankAccountNotFoundException("ACCOUNT NOT FOUND");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(id, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationsDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationsDTOS);
        accountHistoryDTO.setId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

}
