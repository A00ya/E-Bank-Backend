package ma.emsi.ebank.web;

import ma.emsi.ebank.dtos.AccountHistoryDTO;
import ma.emsi.ebank.dtos.AccountOperationDTO;
import ma.emsi.ebank.dtos.BankAccountDTO;
import ma.emsi.ebank.exceptions.BankAccountNotFoundException;
import ma.emsi.ebank.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService){
        this.bankAccountService=bankAccountService;
    }

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
     return bankAccountService.getBankAccount(id);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.listBankAccount();
    }
    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String id){
        return bankAccountService.accountHistory(id);
    }
    //pagination
    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String id, @RequestParam(name="page", defaultValue = "0") int page, @RequestParam(name="size", defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(id,page,size);
    }
}
