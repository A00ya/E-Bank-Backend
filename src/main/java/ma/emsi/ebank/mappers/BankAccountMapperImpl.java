package ma.emsi.ebank.mappers;

import ma.emsi.ebank.dtos.AccountOperationDTO;
import ma.emsi.ebank.dtos.CurrentBankAccountDTO;
import ma.emsi.ebank.dtos.CustomerDTO;
import ma.emsi.ebank.dtos.SavingBankAccountDTO;
import ma.emsi.ebank.entities.AccountOperation;
import ma.emsi.ebank.entities.CurrentAccount;
import ma.emsi.ebank.entities.Customer;
import ma.emsi.ebank.entities.SavingsAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        //customerDTO.setId(customer.getId());
        //customerDTO.setName(customer.getName());
        //customerDTO.setEmail(customerDTO.getEmail());
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingsAccount savingsAccount){
    SavingBankAccountDTO savingBankAccountDTO=new SavingBankAccountDTO();
    BeanUtils.copyProperties(savingsAccount, savingBankAccountDTO);
    savingBankAccountDTO.setCustomerDTO(fromCustomer(savingsAccount.getCustomer()));
    savingBankAccountDTO.setType(savingsAccount.getClass().getSimpleName());
    return savingBankAccountDTO;
    }

    public SavingsAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
    SavingsAccount savingsAccount = new SavingsAccount();
    BeanUtils.copyProperties(savingBankAccountDTO,savingsAccount);
    savingsAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
    return savingsAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
    CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
    BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
    currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
    return currentBankAccountDTO;
    }

    public CurrentAccount  fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
    CurrentAccount currentAccount = new CurrentAccount();
    BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
    currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
    return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
    AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
    BeanUtils.copyProperties(accountOperation, accountOperationDTO);
    return accountOperationDTO;
    }
}
