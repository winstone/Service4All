package cn.edu.buaa.act.service4all.bpmndeployment.endpoints;

import org.act.sdp.appengine.cmp.AppEngineContext;
import org.act.sdp.appengine.cmp.Receiver;
import org.act.sdp.appengine.messaging.ExchangeContext;
import org.act.sdp.appengine.transaction.exception.MessageExchangeInvocationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import cn.edu.buaa.act.service4all.bpmndeployment.BPMNBusinessUnit;
import cn.edu.buaa.act.service4all.bpmndeployment.BPMNReceiver;
import cn.edu.buaa.act.service4all.bpmndeployment.exception.BPMNDeploymentException;
import cn.edu.buaa.act.service4all.bpmndeployment.exception.BPMNUndeploymentException;
import cn.edu.buaa.act.service4all.bpmndeployment.message.BPMNDeploymentMsgFactory;
import cn.edu.buaa.act.service4all.bpmndeployment.message.BPMNDeploymentMsgParser;
import cn.edu.buaa.act.service4all.bpmndeployment.message.BPMNUndeploymentMsgFactory;
import cn.edu.buaa.act.service4all.bpmndeployment.message.BPMNUndeploymentMsgParser;
import cn.edu.buaa.act.service4all.bpmndeployment.task.ServiceDeployTask;
import cn.edu.buaa.act.service4all.bpmndeployment.task.ServiceUndeployTask;
import cn.edu.buaa.act.service4all.bpmndeployment.task.Task;

public class BPMNUndeploymentReceiver extends BPMNReceiver {

	private Log logger = LogFactory.getLog(BPMNUndeploymentReceiver.class);
	
	@Override
	public Document createResponseDocument(ExchangeContext context)
			throws MessageExchangeInvocationException {
		// TODO Auto-generated method stub
		logger.info("Create the response message document for service deployment");
		Task task = (Task)context.getData(BPMNBusinessUnit.TASK);
		
		return factory.createResponseMsg(task);
	}

	@Override
	public void handlRequest(Document request, ExchangeContext context)
			throws MessageExchangeInvocationException {
		// TODO Auto-generated method stub
		if(this.parser == null){
			logger.error("The parser is null, so can't parse the message");
			throw new MessageExchangeInvocationException("The parser is null, so can't parse the message");
		}
		
		if(this.factory == null){
			logger.error("The factory is null, so can't parse the message");
			throw new MessageExchangeInvocationException("The factory is null, so can't parse the message");
		}
		
		if(parser.validate(request)){
			
			Task t = parser.parse(request);
			
			if(t instanceof ServiceUndeployTask){
				
				ServiceUndeployTask task = (ServiceUndeployTask)t;
				context.storeData(BPMNBusinessUnit.TASK, task);
				this.unit.dispatch(context);
				
			}else{
				//the created task is not ServiceDeployTask
				logger.warn("The created task is not type of ServiceDeployTask");
				//throw new MessageExchangeInvocationException("The created task is not type of ServiceDeployTask");
				BPMNUndeploymentException e = new BPMNUndeploymentException("The created task is not type of ServiceUndeployTask");
				t.setException(e);
				//send the response message
				Document excepDoc = factory.createResponseMsg(t);
				this.sendResponseMessage(excepDoc, context);
			}
			
		}else{
			
			Document excepDoc = factory.createExceptionDocument("The message for service undeployment is invalidate");
			this.sendResponseMessage(excepDoc, context);
		}
		
	}
	
	protected void initParser(AppEngineContext context){
		//initiate the message parser
		this.parser = new BPMNUndeploymentMsgParser();
	}
	
	protected void initFactory(AppEngineContext context){
		//init the factory
		this.factory = new BPMNUndeploymentMsgFactory();
	}

}
