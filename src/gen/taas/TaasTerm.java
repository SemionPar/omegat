//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.05.23 at 07:35:02 AM FET 
//


package gen.taas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="term" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entryID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="collectionID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="collectionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="collectionType" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="domainID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="domainName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "term",
    "entryID",
    "collectionID",
    "collectionName",
    "collectionType",
    "domainID",
    "domainName",
    "language"
})
@XmlRootElement(name = "term")
public class TaasTerm {

    @XmlElement(required = true)
    protected String term;
    protected long entryID;
    protected long collectionID;
    @XmlElement(required = true)
    protected String collectionName;
    protected long collectionType;
    @XmlElement(required = true)
    protected String domainID;
    @XmlElement(required = true)
    protected String domainName;
    @XmlElement(required = true)
    protected String language;

    /**
     * Gets the value of the term property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerm() {
        return term;
    }

    /**
     * Sets the value of the term property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerm(String value) {
        this.term = value;
    }

    /**
     * Gets the value of the entryID property.
     * 
     */
    public long getEntryID() {
        return entryID;
    }

    /**
     * Sets the value of the entryID property.
     * 
     */
    public void setEntryID(long value) {
        this.entryID = value;
    }

    /**
     * Gets the value of the collectionID property.
     * 
     */
    public long getCollectionID() {
        return collectionID;
    }

    /**
     * Sets the value of the collectionID property.
     * 
     */
    public void setCollectionID(long value) {
        this.collectionID = value;
    }

    /**
     * Gets the value of the collectionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * Sets the value of the collectionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionName(String value) {
        this.collectionName = value;
    }

    /**
     * Gets the value of the collectionType property.
     * 
     */
    public long getCollectionType() {
        return collectionType;
    }

    /**
     * Sets the value of the collectionType property.
     * 
     */
    public void setCollectionType(long value) {
        this.collectionType = value;
    }

    /**
     * Gets the value of the domainID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainID() {
        return domainID;
    }

    /**
     * Sets the value of the domainID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainID(String value) {
        this.domainID = value;
    }

    /**
     * Gets the value of the domainName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Sets the value of the domainName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainName(String value) {
        this.domainName = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

}