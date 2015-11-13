package org.grails.orm.hibernate

import grails.persistence.Entity;

import org.junit.Test

import static junit.framework.Assert.*
/**
 * @author Graeme Rocher
 * @since 1.0
 *
 * Created: May 29, 2008
 */
class CascadeToCompositeIdAssociationTests extends AbstractGrailsHibernateTests {


    @Test
    void testCascadeToCompositeIdEntity() {
        def trade = new CascadeToCompositeIdAssociationTrade()
        def segment = new CascadeToCompositeIdAssociationSegment()

        def product = new CascadeToCompositeIdAssociationProduct()
        def country = new CascadeToCompositeIdAssociationCountry(name:"UK")
        assertNotNull country.save(flush:true)
        product.country = country
        segment.addToProducts(product)
        trade.addToSegments(segment)

        assertNotNull trade.save(flush:true)

        session.clear()

        assert product.validate()
        assertEquals 1, CascadeToCompositeIdAssociationTrade.count()
        assertEquals 1, CascadeToCompositeIdAssociationSegment.count()
        assertEquals 1, CascadeToCompositeIdAssociationProduct.count()
        assertEquals 1, CascadeToCompositeIdAssociationCountry.count()
    }

    @Override
    protected getDomainClasses() {
        [CascadeToCompositeIdAssociationCountry, CascadeToCompositeIdAssociationProduct, CascadeToCompositeIdAssociationSegment, CascadeToCompositeIdAssociationTrade]
    }
}

@Entity
class CascadeToCompositeIdAssociationTrade {
    Long id
    Long version
    Set segments
    static hasMany = [segments:CascadeToCompositeIdAssociationSegment]
}

@Entity
class CascadeToCompositeIdAssociationSegment{
    Long id
    Long version
    Set products
    static hasMany = [products:CascadeToCompositeIdAssociationProduct]
}

@Entity
class CascadeToCompositeIdAssociationProduct implements Serializable{
    Long id
    Long version

    CascadeToCompositeIdAssociationCountry country
    CascadeToCompositeIdAssociationSegment segment

    static mapping = {
        id composite:['country','segment']
    }
}

@Entity
class CascadeToCompositeIdAssociationCountry implements Serializable{
    Long id
    Long version
    String name
}