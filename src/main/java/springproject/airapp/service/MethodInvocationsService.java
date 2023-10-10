package coherentsolutions.airapp.service;

import coherentsolutions.airapp.model.entity.MethodInvocations;
import coherentsolutions.airapp.repository.MethodInvocationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MethodInvocationsService {
    private final MethodInvocationsRepository methodInvocationsRepository;
    public void save(MethodInvocations methodInvocations) {
        methodInvocationsRepository.save(methodInvocations);
    }

}
