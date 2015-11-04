package it.bsdsoftware.dynamicquestion.library.models;

import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.response.BSDResponse;

/**
 * Created by Simone on 03/11/15.
 */
public interface CallbackComplete {
    void doOnComplete(List<? extends BSDResponse> response);
}
