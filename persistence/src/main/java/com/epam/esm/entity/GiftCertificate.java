package com.epam.esm.entity;

import com.epam.esm.entity.audit.GiftCertificateAudit;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Entity
@Table(name = "gift_certificates")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Audited
@EntityListeners(GiftCertificateAudit.class)
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @NotAudited
    @ManyToMany
    @JoinTable(name = "m2m_gift_certificates_tags", joinColumns = @JoinColumn(name = "gift_certificate_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;

        if (id==null){
            if (that.id!=null){
                return false;
            }
        }else if (!id.equals(that.id)){
            return false;
        }
        if (name==null){
            if (that.name!=null){
                return false;
            }
        }else if (!name.equals(that.name)){
            return false;
        }
        if (description==null){
            if (that.description!=null){
                return false;
            }
        }else if (!description.equals(that.description)){
            return false;
        }
        if (price==null){
            if (that.price!=null){
                return false;
            }
        }else if (!price.equals(that.price)){
            return false;
        }
        if (createDate==null){
            if (that.createDate!=null){
                return false;
            }
        }else if (!createDate.equals(that.createDate)){
            return false;
        }
        if (lastUpdateDate==null){
            if (that.lastUpdateDate!=null){
                return false;
            }
        }else if (!lastUpdateDate.equals(that.lastUpdateDate)){
            return false;
        }
        if (tags==null){
            if (that.tags!=null){
                return false;
            }
        }else if (!tags.equals(that.tags)){
            return false;
        }

        return duration == that.duration;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + createDate.hashCode();
        result = 31 * result + lastUpdateDate.hashCode();
        return result;
    }
}
