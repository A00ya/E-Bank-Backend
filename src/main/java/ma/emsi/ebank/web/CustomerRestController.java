package ma.emsi.ebank.web;

import ma.emsi.ebank.dtos.CustomerDTO;

import ma.emsi.ebank.exceptions.CustomerNotFoundException;
import ma.emsi.ebank.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.customerslist();
    }
    @GetMapping("/customer/{id}")
    public CustomerDTO getCustomer(@PathVariable(name ="id") long customerId) throws CustomerNotFoundException {

        return bankAccountService.getCustomer(customerId);
    }

    @GetMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){

        return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){
    customerDTO.setId(id);
    return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(Long id){
     bankAccountService.deleteCustomer(id);
    }
}
