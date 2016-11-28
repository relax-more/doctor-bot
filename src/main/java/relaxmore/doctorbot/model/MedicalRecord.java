package relaxmore.doctorbot.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
public class MedicalRecord {
    Period period;
    List<Symptom> symptoms;
    Instant requestTime;

    public enum Period {
        less12h,
        less24h,
        less36h,
        greater36h
    }

    public enum Symptom {
        cough,
        fever,
        eczema
    }

}
