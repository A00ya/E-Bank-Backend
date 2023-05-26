package ma.emsi.ebank.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.ebank.enums.AccountStatus;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data @AllArgsConstructor @NoArgsConstructor
@DiscriminatorColumn(name = "TYPE", length = 4)
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    //Lazy: charger les comptes mais machi les operations
    //Eager : kaydir koulchi drba w7da, mais 7ADARI, ga3 les operations yjiw and thats a big no no
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperationList;
}
