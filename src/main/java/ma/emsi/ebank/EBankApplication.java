package ma.emsi.ebank;

import ma.emsi.ebank.entities.*;
import ma.emsi.ebank.enums.AccountStatus;
import ma.emsi.ebank.enums.OperationType;
import ma.emsi.ebank.repositories.AccountOperationRepository;
import ma.emsi.ebank.repositories.BankAccountRepository;
import ma.emsi.ebank.repositories.CustomerRepository;
import ma.emsi.ebank.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBankApplication.class, args);
    }

   @Bean
    CommandLineRunner commandLineRunner(BankService bankService){
        return args ->{
            bankService.consulter();
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Aya","Hassan","Yassine").forEach(name->{
                Customer customer= new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingsAccount savingaccount = new SavingsAccount();
                savingaccount.setBalance(Math.random()*9000);
                savingaccount.setId(UUID.randomUUID().toString());
                savingaccount.setCreatedAt(new Date());
                savingaccount.setStatus(AccountStatus.CREATED);
                savingaccount.setCustomer(cust);
                savingaccount.setInterestRate(5.5);
                bankAccountRepository.save(savingaccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i=0; i < 5; i++){
                    AccountOperation accountOperation= new AccountOperation();
                    accountOperation.setOpDate(new Date());
                    accountOperation.setAmount(Math.random()*120000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }


            });


        };
    }
}
