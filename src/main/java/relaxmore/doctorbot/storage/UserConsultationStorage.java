package relaxmore.doctorbot.storage;

import org.springframework.stereotype.Repository;
import relaxmore.doctorbot.model.MedicalRecord;

import java.time.Instant;
import java.util.Map;

@Repository
public class UserConsultationStorage {
    private Map<String, MedicalRecord> recordMap; // TODO: save to storage

    public MedicalRecord get(String userId){
        return recordMap.getOrDefault(userId, new MedicalRecord().setRequestTime(Instant.now()));
    }

    public MedicalRecord update(String userId, MedicalRecord medicalRecord){
        return recordMap.put(userId, medicalRecord);
    }

}
