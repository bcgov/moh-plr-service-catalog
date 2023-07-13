package org.hl7.fhir.r4bc1.model;


import static hlth.gov.bc.ca.serviceCatalog.ServiceCatalogConstant.*;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.model.api.annotation.Extension;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.Reference;

/**
 * General constraints on the BC Catalog HealthcareService resource for use in the BC Provider
 * Registry project.
 *
 * @author camille.estival
 */
@ResourceDef(profile = BC_CATALOG_SERVICE_PROFILE_URL, id = "bc-practitioner")
public class BCCatalogService extends HealthcareService {

    
    /**
     * A custom complex extension for BC Notes Extension with @Extension
     * annotation
     */
    @Child(name = "offeredIn", min = 0, max = 1)
    @Extension(url = OFFEREDIN_R5_URL, definedLocally = false, isModifier = false)
    @Description(shortDefinition = "offeredIn", formalDefinition = "Offered In, parent relationship to another BCCatalogService")
    protected Reference offeredInExtension;

    public Reference getOfferedInExtension() {
        return offeredInExtension;
    }

    public void setOfferedInExtension(Reference offeredIn) {
        offeredInExtension = offeredIn;
    }
    
//    /**
//     * A custom complex extension for BC Notes Extension with @Extension
//     * annotation
//     */
//    @Child(name = "bc-note-extension", min = 0, max = -1)
//    @Extension(url = BC_NOTE_EXTENSION_URL, definedLocally = false, isModifier = false)
//    @Description(shortDefinition = "BC Note extension", formalDefinition = "BC Notes")
//    protected List<BCNoteExtension> bcNoteExtension;
//
//    public List<BCNoteExtension> getBcNoteExtension() {
//        if (bcNoteExtension == null) {
//            bcNoteExtension = new ArrayList<>();
//        }
//        return bcNoteExtension;
//    }
//
//    public void addBcNoteExtension(BCNoteExtension note) {
//        this.getBcNoteExtension().add(note);
//    }
//
//    public void setBcNoteExtension(List<BCNoteExtension> myBcNoteExtension) {
//        bcNoteExtension = myBcNoteExtension;
//    }

//    @Child(name = "period")
//    @Extension(url = BC_PERIOD_EXTENSION_URL, definedLocally = false, isModifier = false)
//    @Description(shortDefinition = "Effective dates")
//    protected Period period;
//
//    public Period getPeriod() {
//        if (period == null) {
//            period = new Period();
//        }
//        return period;
//    }
//
//    public void setPeriod(Period thePeriod) {
//        period = thePeriod;
//    }

    /**
     * It is important to override the isEmpty() method, adding a check for any
     * newly added fields.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(active, contained );
    }

    @Override
    public BCCatalogService copy() {
        BCCatalogService copy = new BCCatalogService();
        copy.active = active;
        copy.contained = contained;
//        copy.period = period;
//        copy.bcNoteExtension = bcNoteExtension;
//        copy.owner = owner;
//        copy.endReason = endReason;
        return copy;
    }

}
