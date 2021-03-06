package cn.edu.buaa.act.service4all.bpmndeployment.Deploy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.act.sdp.appengine.cmp.Receiver;
import org.act.sdp.appengine.messaging.ExchangeContext;
import org.act.sdp.appengine.transaction.exception.MessageExchangeInvocationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cn.edu.buaa.act.service4all.bpmndeployment.File.Constants;


public class BPMNDeployReceiver extends Receiver
{

	@Override
	public void handlRequest(Document doc, ExchangeContext context) 
	{
		// TODO Auto-generated method stub
		logger.info("Receiving the document!");
		getInformation(doc,context);		
		this.unit.dispatch(context);	
	}
	
	private void getInformation(Document doc,ExchangeContext context)
	{
		Element root;
		
		root=doc.getDocumentElement();
		
		String fileName = root.getElementsByTagName(Constants.filename).item(0).getTextContent();
		
		
		Element bpmnData = (Element)root.getElementsByTagName(Constants.bpmndata).item(0);
		Element bpmnElement = (Element)bpmnData.getFirstChild();
		Document bpmnDoc = newDocumentFromElement(bpmnElement);
		
		context.storeData(Constants.filename, fileName);
		context.storeData(Constants.bpmndata, bpmnDoc);
	}

	private Document newDocumentFromElement(Element e){
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = f.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element nElement = (Element)doc.importNode(e, true);
			doc.appendChild(nElement);
			
			return doc;
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Document createResponseDocument(ExchangeContext context)	throws MessageExchangeInvocationException 
	{
		Document doc;
		doc=(Document) context.getData(Constants.deployfeedbackresponse);
		return doc;
	}
}