package com.eximbills.backservice2;

import com.eximbills.backservice2.domain.Balance;
import com.eximbills.backservice2.domain.Entry;
import com.eximbills.backservice2.repository.BalanceRepository;
import com.eximbills.backservice2.repository.EntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@EnableAutoConfiguration
public class Backservice2Application {

    private static final Logger logger = LoggerFactory.getLogger(Backservice2Application.class);

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private EntryRepository entryRepository;

    public static void main(String[] args) {
        SpringApplication.run(Backservice2Application.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    @Transactional
    String home() {
        logger.debug("Request for /");
        Balance balance = new Balance(1L, (float) 1.00, "N");
        String entryId = UUID.randomUUID().toString();
        Entry entry = new Entry(entryId, (float) 1.00, balance);
        balanceRepository.save(balance);
        entryRepository.save(entry);
        balanceRepository.findById(1L).ifPresent(bal -> {
            logger.debug("Balance found with findById(1L):" + bal.toString());
        });
        entryRepository.findById(entryId).ifPresent(ent -> {
            logger.debug("Entry: " + ent.toString());
        });
        return "Servie 2 listening on port 8082...";
    }

    @GetMapping("/balance/{id}")
    @ResponseBody
    @Transactional(readOnly = true)
    public Balance getBalanceInfo(@PathVariable("id") Long id) {
        return balanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No balance found with id=" + id));
    }

    @PutMapping("/balance/{id}/{amount}/{transactionId}")
    @Transactional
    public ResponseEntity<Entry> postEntry(@PathVariable("id") Long id, @PathVariable("amount") float amount,
                                           @PathVariable("transactionId") String transactionId) {
        Balance balance;
        try {
            balance = balanceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No balance found with id=" + id));
        } catch (ResourceNotFoundException e) {
            logger.debug(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        if (balance.getHoldFlag().equals("Y")) {
            logger.debug("Balance is hold by other people");
            return new ResponseEntity<>(null, HttpStatus.LOCKED);
        }
        balance.setBalance(balance.getBalance() + amount);
        balance.setHoldFlag("Y");
        Entry entry = new Entry(transactionId, amount, balance);
        balanceRepository.save(balance);
        return new ResponseEntity<>(entryRepository.save(entry), HttpStatus.OK);
    }

    @PutMapping("/balance/{id}")
    @ResponseBody
    @Transactional
    public Balance unHold(@PathVariable("id") Long id) {
        Balance balance = balanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No balance found with id=" + id));
        balance.setHoldFlag("N");
        return balanceRepository.save(balance);
    }

    @PutMapping("/balance/{id}/{transactionId}")
    @ResponseBody
    @Transactional
    public Entry reverseEntry(@PathVariable("id") Long id,
                              @PathVariable("transactionId") String transactionId) {
        Entry entry = entryRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("No entry found with id=" + id));
        Balance balance = entry.getBalance();
        balance.setBalance(balance.getBalance() - entry.getEntryAmount());
        balance.setHoldFlag("N");
        entry.setEntryAmount(0);
        balanceRepository.save(balance);
        return entryRepository.save(entry);
    }

}
