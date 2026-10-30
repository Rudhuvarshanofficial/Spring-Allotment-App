
package com.tnea.counselling.dto;

import com.tnea.counselling.model.Preference;

public record PreferenceResponse(
        Integer id,
        Integer priority,
        Integer collegeid,
        Integer courseid
) {
    public static PreferenceResponse from(Preference preference) {
        return new PreferenceResponse(
                preference.getId(),
                preference.getPriority(),
                preference.getCollegeid(),
                preference.getCourseid()
        );
    }
}
