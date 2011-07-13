package com.zuora.api.object;

/**
 * Generated class, please do not edit.
 */
public class CreateResponse_element implements com.sforce.ws.bind.XMLizable {

  /**
   * Constructor
   */
  public CreateResponse_element() {
  }
    
  
  /**
   * element  : result of type {http://api.zuora.com/}SaveResult
   * java type: com.sforce.soap.SaveResult[]
   */
  private static final com.sforce.ws.bind.TypeInfo result__typeInfo =
    new com.sforce.ws.bind.TypeInfo("http://api.zuora.com/","result","http://api.zuora.com/","SaveResult",0,-1,true);

  private boolean result__is_set = false;

  private com.zuora.api.object.SaveResult[] result = new com.zuora.api.object.SaveResult[0];

  public com.zuora.api.object.SaveResult[] getResult() {
    return result;
  }

  

  public void setResult(com.zuora.api.object.SaveResult[] result) {
    this.result = result;
    result__is_set = true;
  }
  

  /**
   */
  public void write(javax.xml.namespace.QName __element,
      com.sforce.ws.parser.XmlOutputStream __out, com.sforce.ws.bind.TypeMapper __typeMapper)
      throws java.io.IOException {
    __out.writeStartTag(__element.getNamespaceURI(), __element.getLocalPart());
    
    writeFields(__out, __typeMapper);
    __out.writeEndTag(__element.getNamespaceURI(), __element.getLocalPart());
  }

  protected void writeFields(com.sforce.ws.parser.XmlOutputStream __out,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException {
   
    __typeMapper.writeObject(__out, result__typeInfo, result, result__is_set);
  }


  public void load(com.sforce.ws.parser.XmlInputStream __in,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
    __typeMapper.consumeStartTag(__in);
    loadFields(__in, __typeMapper);
    __typeMapper.consumeEndTag(__in);
  }

  protected void loadFields(com.sforce.ws.parser.XmlInputStream __in,
      com.sforce.ws.bind.TypeMapper __typeMapper) throws java.io.IOException, com.sforce.ws.ConnectionException {
   
    __in.peekTag();
    if (__typeMapper.isElement(__in, result__typeInfo)) {
      setResult((com.zuora.api.object.SaveResult[])__typeMapper.readObject(__in, result__typeInfo, com.zuora.api.object.SaveResult[].class));
    }
  }

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append("[CreateResponse_element ");
    
    sb.append(" result=");
    sb.append("'"+com.sforce.ws.util.Verbose.toString(result)+"'\n");
    sb.append("]\n");
    return sb.toString();
  }
}