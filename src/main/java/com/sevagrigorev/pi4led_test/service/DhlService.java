package com.sevagrigorev.pi4led_test.service;

//import com.sevagrigorev.pi4led_test.model.DHL;
//import com.sevagrigorev.pi4led_test.repository.DhlRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;

//@Service
//public class DhlService {
//
//    @Autowired
//    private DhlRepository repository;
//
//    public void create(DHL dhl) {
//        repository.save(dhl);
//    }
//
//    public List<DHL> getAll() {
//        return repository.findAll();
//    }
//
//    public DHL get(int id) {
//        return repository.getOne(id);
//    }
//
//    public boolean update(DHL dhl, int id) {
//        if (repository.existsById(id)) {
//            dhl.setId(id);
//            repository.save(dhl);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean delete(int id) {
//        if (repository.existsById(id)) {
//            repository.deleteById(id);
//            return true;
//        }
//        return false;
//    }
//
//}
