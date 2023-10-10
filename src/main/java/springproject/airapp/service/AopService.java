package coherentsolutions.airapp.service;

import coherentsolutions.airapp.annotations.Audit;
import org.springframework.stereotype.Service;

@Service
public class AopService {

    @Audit
    public void sayMessage() {
        System.out.println(("I'm saying something!"));
    }

    @Audit
    public String makeToUpperCase(String abcd) {
        return abcd.toUpperCase();
    }

    @Audit
    public void errorAppears() {
        throw new NullPointerException("Ops, null pointer exception!");
    }
}
