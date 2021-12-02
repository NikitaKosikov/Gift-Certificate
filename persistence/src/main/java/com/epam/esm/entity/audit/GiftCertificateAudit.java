package com.epam.esm.entity.audit;

import com.epam.esm.entity.GiftCertificate;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class GiftCertificateAudit {

    /**
     * Performed before the persist operation and sets the gift certificate creation and last updated date
     *
     * @param giftCertificate the gift certificate in which we set creation and last updated date
     */
    @PrePersist
    public void beforeCreateGiftCertificate(GiftCertificate giftCertificate) {
        LocalDateTime currentTime = LocalDateTime.now();
        giftCertificate.setCreateDate(currentTime);
        giftCertificate.setLastUpdateDate(currentTime);
    }

    /**
     * Performed before the update operation
     * and sets the gift certificate creation and last updated date
     *
     * @param giftCertificate the gift certificate in which we set creation and last updated date
     */
    @PreUpdate
    public void beforeUpdateGiftCertificate(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

}
