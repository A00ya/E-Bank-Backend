package ma.emsi.ebank.services;


import jakarta.transaction.Transactional;
import ma.emsi.ebank.entities.BankAccount;
import ma.emsi.ebank.entities.CurrentAccount;
import ma.emsi.ebank.entities.SavingsAccount;
import ma.emsi.ebank.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){

        BankAccount bankAccount=bankAccountRepository.findById("39005c9a-a181-40b4-ac9e-b409e82c2003").orElse(null);


            if(bankAccount!=null) {
                System.out.println("**********************************");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("Over DRAFT" + ((CurrentAccount) bankAccount).getOverDraft());
                } else if (bankAccount instanceof SavingsAccount) {
                    System.out.println("Interest RATE" + ((SavingsAccount) bankAccount).getInterestRate());
                }

                bankAccount.getAccountOperationList().forEach(op -> {
                    System.out.println("**********************");
                    System.out.println(op.getType());
                    System.out.println(op.getOpDate());
                    System.out.println(op.getAmount());
                });
            }
    }
}
