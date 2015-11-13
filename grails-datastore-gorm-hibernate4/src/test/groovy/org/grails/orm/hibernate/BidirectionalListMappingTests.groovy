package org.grails.orm.hibernate

import grails.core.DefaultGrailsApplication
import org.grails.orm.hibernate.cfg.DefaultGrailsDomainConfiguration
import org.hibernate.FetchMode
import org.hibernate.engine.spi.CascadeStyles
import org.hibernate.mapping.Backref
import org.hibernate.mapping.Column
import org.hibernate.mapping.DependantValue
import org.hibernate.mapping.IndexBackref
import org.hibernate.mapping.KeyValue
import org.hibernate.mapping.ManyToOne
import org.hibernate.mapping.OneToMany
import org.hibernate.mapping.PersistentClass
import org.hibernate.mapping.Property
import org.hibernate.mapping.SimpleValue
import org.hibernate.mapping.Table
import org.hibernate.type.IntegerType
import org.hibernate.type.LongType
import org.hibernate.type.ManyToOneType


import static junit.framework.Assert.*

 /**
 * @author Graeme Rocher
 * @since 1.0
 */
class BidirectionalListMappingTests extends GroovyTestCase {

    DefaultGrailsDomainConfiguration config

    protected void setUp() {
        ExpandoMetaClass.enableGlobally()

        def gcl = new GroovyClassLoader()

        config = new DefaultGrailsDomainConfiguration()

        DefaultGrailsApplication application = new DefaultGrailsApplication([TestFaqElement, TestFaqSection] as Class[], gcl)
        application.initialise()
        config.grailsApplication = application

        config.buildMappings()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndexBackrefMapping() {
        PersistentClass faqSection = config.getClassMapping(TestFaqSection.name)
        PersistentClass faqElement = config.getClassMapping(TestFaqElement.name)
        IndexBackref elementsIndexBackref = faqElement.getProperty("_elementsIndexBackref")

        assertTrue elementsIndexBackref.isBackRef()
        assertFalse elementsIndexBackref.isBasicPropertyAccessor()
        assertFalse elementsIndexBackref.isComposite()
        assertTrue elementsIndexBackref.isInsertable()
        assertFalse elementsIndexBackref.isLazy()
        assertFalse elementsIndexBackref.isNaturalIdentifier()
        assertTrue elementsIndexBackref.isOptimisticLocked()
        assertTrue elementsIndexBackref.isOptional()
        assertFalse elementsIndexBackref.isSelectable()
        assertFalse elementsIndexBackref.isSelectable()

        assertNull elementsIndexBackref.getCascade()
        assertEquals CascadeStyles.NONE, elementsIndexBackref.getCascadeStyle()
        assertEquals "${TestFaqSection.name}.elements".toString(), elementsIndexBackref.getCollectionRole()
        assertEquals 1, elementsIndexBackref.getColumnSpan()
        assertEquals TestFaqSection.name, elementsIndexBackref.getEntityName()
        assertEquals "_elementsIndexBackref", elementsIndexBackref.getName()
        assertNull elementsIndexBackref.getNodeName()
        assertNull elementsIndexBackref.getPropertyAccessorName()
        assertEquals IntegerType, elementsIndexBackref.getType().getClass()
        assertEquals SimpleValue, elementsIndexBackref.getValue().getClass()

        SimpleValue value = elementsIndexBackref.getValue()

        assertFalse value.isAlternateUniqueKey()
        assertFalse value.isCascadeDeleteEnabled()
        assertTrue value.isNullable()
        assertTrue value.isSimpleValue()
        assertTrue value.isTypeSpecified()
        assertTrue value.isUpdateable()
    }

    void testCollectionBackrefMapping() {
        PersistentClass faqSection = config.getClassMapping(TestFaqSection.name)
        PersistentClass faqElement = config.getClassMapping(TestFaqElement.name)
        Backref elementsBackref =faqElement.getProperty("_TestFaqSection_elementsBackref")

        assertTrue elementsBackref.isBackRef()
        assertFalse elementsBackref.isBasicPropertyAccessor()
        assertFalse elementsBackref.isComposite()
        assertTrue elementsBackref.isInsertable()
        assertFalse elementsBackref.isLazy()
        assertFalse elementsBackref.isNaturalIdentifier()
        assertTrue elementsBackref.isOptimisticLocked()
        assertFalse elementsBackref.isOptional()
        assertFalse elementsBackref.isSelectable()
        assertFalse elementsBackref.isUpdateable()

        assertNull elementsBackref.getCascade()
        assertEquals CascadeStyles.NONE, elementsBackref.getCascadeStyle()
        assertEquals "${TestFaqSection.name}.elements".toString(), elementsBackref.getCollectionRole()
        assertEquals 1, elementsBackref.getColumnSpan()
        assertEquals TestFaqSection.name, elementsBackref.getEntityName()
        assertEquals "_TestFaqSection_elementsBackref", elementsBackref.getName()
        assertNull elementsBackref.getNodeName()
        assertEquals TestFaqElement.name, elementsBackref.getPersistentClass().getClassName()
        assertNull elementsBackref.getPropertyAccessorName()
        assertEquals LongType, elementsBackref.getType().getClass()
        assertEquals DependantValue, elementsBackref.getValue().getClass()

        DependantValue value = elementsBackref.getValue()

        assertFalse value.isAlternateUniqueKey()
        assertFalse value.isCascadeDeleteEnabled()
        assertFalse value.isNullable()
        assertTrue value.isSimpleValue()
        assertFalse value.isTypeSpecified()
        assertTrue value.isUpdateable()
        assertEquals 1,value.getColumnInsertability().size()
        assertTrue value.getColumnInsertability()[0]
        assertEquals 1,value.getColumnUpdateability().size()
        assertTrue value.getColumnUpdateability()[0]

        assertEquals 1, value.getColumnSpan()
        assertEquals FetchMode.SELECT, value.getFetchMode()
        assertNull value.getForeignKeyName()
        assertEquals "assigned", value.getIdentifierGeneratorStrategy()
        assertNull value.getNullValue()
        assertEquals LongType, value.getType().getClass()
    }

    void testManySidePropertyMapping() {
        PersistentClass faqSection = config.getClassMapping(TestFaqSection.name)
        PersistentClass faqElement = config.getClassMapping(TestFaqElement.name)
        Property section = faqElement.getProperty("section")

        assertFalse section.isBackRef()
        assertFalse section.isComposite()
        assertFalse section.isInsertable()
        assertFalse section.isLazy()
        assertFalse section.isNaturalIdentifier()
        assertTrue section.isOptimisticLocked()
        assertFalse section.isOptional()
        assertTrue section.isSelectable()
        assertFalse section.isUpdateable()

        assertEquals "none", section.getCascade()
        assertEquals CascadeStyles.NONE, section.getCascadeStyle()
        assertEquals 1, section.getColumnSpan()
        assertEquals "section", section.getName()
    }

    void testManySideColumnMapping() {
        PersistentClass faqSection = config.getClassMapping(TestFaqSection.name)
        PersistentClass faqElement = config.getClassMapping(TestFaqElement.name)
        Property section = faqElement.getProperty("section")
        Column sectionColumn = section.getColumnIterator().next()

        assertEquals "section_id", sectionColumn.getCanonicalName()
        assertNull sectionColumn.getCheckConstraint()
        assertNull sectionColumn.getComment()
        assertNull sectionColumn.getDefaultValue()
        assertEquals 255, sectionColumn.getLength()
        assertEquals "section_id", sectionColumn.getName()
        assertEquals 19, sectionColumn.getPrecision()
        assertEquals "section_id", sectionColumn.getQuotedName()
        assertEquals 2, sectionColumn.getScale()
        assertEquals "section_id", sectionColumn.getText()
        assertEquals 0, sectionColumn.getTypeIndex()
    }

    void testManyToOneMapping() {
        PersistentClass faqSection = config.getClassMapping(TestFaqSection.name)
        PersistentClass faqElement = config.getClassMapping(TestFaqElement.name)
        Property section = faqElement.getProperty("section")

        ManyToOne manyToOne = section.getValue()
        assertEquals 1,manyToOne.getColumnInsertability().size()
        assertTrue manyToOne.getColumnInsertability()[0]
        assertEquals 1,manyToOne.getColumnUpdateability().size()
        assertTrue manyToOne.getColumnUpdateability()[0]

        assertFalse manyToOne.isAlternateUniqueKey()
        assertFalse manyToOne.isCascadeDeleteEnabled()
        assertFalse manyToOne.isEmbedded()
        assertTrue manyToOne.isLazy()
        assertFalse manyToOne.isNullable()
        assertTrue manyToOne.isSimpleValue()
        assertTrue manyToOne.isTypeSpecified()
        assertFalse manyToOne.isUnwrapProxy()
        assertTrue manyToOne.isUpdateable()

        assertEquals 1, manyToOne.getConstraintColumns().size()
        assertEquals FetchMode.DEFAULT, manyToOne.getFetchMode()
        assertNull manyToOne.getForeignKeyName()
        assertEquals "assigned", manyToOne.getIdentifierGeneratorStrategy()
        assertNull manyToOne.getNullValue()
        assertEquals TestFaqSection.name, manyToOne.getReferencedEntityName()
        assertNull manyToOne.getReferencedPropertyName()
        assertEquals ManyToOneType, manyToOne.getType().getClass()
        assertEquals TestFaqSection.name, manyToOne.getTypeName()
    }

    void testListMapping() {

        org.hibernate.mapping.List list = config.getCollectionMapping("${TestFaqSection.name}.elements")

        assertFalse list.isAlternateUniqueKey()
        assertFalse list.isArray()
        assertFalse list.isCustomDeleteAllCallable()
        assertFalse list.isCustomDeleteCallable()
        assertFalse list.isCustomInsertCallable()
        assertFalse list.isCustomUpdateCallable()
        assertTrue list.isEmbedded()
        assertFalse list.isExtraLazy()
        assertFalse list.isIdentified()
        assertTrue list.isIndexed()
        assertFalse list.isInverse()
        assertTrue list.isLazy()
        assertTrue list.isList()
        assertFalse list.isMap()
        assertTrue list.isMutable()
        assertTrue list.isNullable()
        assertTrue list.isOneToMany()
        assertTrue list.isOptimisticLocked()
        assertFalse list.isPrimitiveArray()
        assertFalse list.isSet()
        assertFalse list.isSorted()
        assertFalse list.isSubselectLoadable()

        assertEquals 0,list.getBaseIndex()
//		assertEquals FaqElement,list.getCollectionPersisterClass()
        Table t = list.getCollectionTable()
        assertNotNull t
        assertEquals 0, list.getColumnInsertability().size()
        assertNull list.getCacheConcurrencyStrategy()
        assertEquals "${TestFaqSection.name}.elements".toString(), list.getCacheRegionName()
        assertEquals 0,list.getColumnSpan()
        assertEquals 0, list.getColumnUpdateability().size()
        assertNull list.getElementNodeName()
        SimpleValue index = list.getIndex()

        assertEquals 1,index.getColumnInsertability().size()
        assertTrue index.getColumnInsertability()[0]
        assertEquals 1,index.getColumnUpdateability().size()
        assertTrue index.getColumnUpdateability()[0]

        assertEquals 1, index.getColumnSpan()

        Column indexColumn = index.getColumnIterator().next()
        assertEquals "elements_idx", indexColumn.getCanonicalName()
        assertNull indexColumn.getCheckConstraint()
        assertNull indexColumn.getComment()
        assertNull indexColumn.getDefaultValue()
        assertEquals 255, indexColumn.getLength()
        assertEquals "elements_idx", indexColumn.getName()
        assertEquals 19, indexColumn.getPrecision()
        assertEquals "elements_idx", indexColumn.getQuotedName()
        assertEquals 2, indexColumn.getScale()
        assertEquals "elements_idx", indexColumn.getText()
        SimpleValue indexColumnValue = indexColumn.getValue()

        assertEquals FetchMode.SELECT, index.getFetchMode()
        assertNull index.getForeignKeyName()
        assertEquals "assigned", index.getIdentifierGeneratorStrategy()
        assertNull index.getNullValue()
        assertEquals IntegerType, index.getType()?.getClass()
        assertEquals "integer", index.getTypeName()
        assertNull index.getTypeParameters()

        KeyValue key = list.getKey()

        assertEquals 1,key.getColumnInsertability().size()
        assertTrue key.getColumnInsertability()[0]
        assertEquals 1,key.getColumnUpdateability().size()
        assertTrue key.getColumnUpdateability()[0]

        assertEquals 1, key.getColumnSpan()
        assertEquals FetchMode.SELECT, key.getFetchMode()
        assertNull key.getNullValue()
        assertEquals LongType, key.getType().getClass()

        OneToMany element = list.getElement()

        assertEquals 1, element.getColumnSpan()
        assertEquals FetchMode.JOIN, element.getFetchMode()
        PersistentClass associatedClass = element.getAssociatedClass()
        assertEquals TestFaqElement.name, associatedClass.getClassName()
        assertEquals ManyToOneType, element.getType().getClass()
    }
}
