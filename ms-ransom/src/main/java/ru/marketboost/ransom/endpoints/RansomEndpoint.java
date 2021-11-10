package ru.marketboost.ransom.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.marketboost.ransom.interfaces.IRunsomService;
import ru.marketboost.ransom.models.requests.RansomRequest;
import ru.marketboost.ransom.tasks.RansomTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class RansomEndpoint implements IRunsomService {

    private final ExecutorService executorService =  Executors.newSingleThreadExecutor();

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @PostMapping(value = MAKE_RANSOM, produces = "application/json")
    public String make(@RequestBody RansomRequest ransomRequest) {
        RansomTask ransomTask = applicationContext.getBean(RansomTask.class);
        ransomTask.init(ransomRequest);

        executorService.submit(ransomTask);

        return "ok";
    }

    @PostMapping(value = "/test", produces = "application/json")
    public String test() {
        return "testing yopta";
    }

}
