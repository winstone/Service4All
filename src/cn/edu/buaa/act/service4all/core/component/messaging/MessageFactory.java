/**
* Service4All: A Service-oriented Cloud Platform for All about Software Development
* Copyright (C) Institute of Advanced Computing Technology, Beihang University
* Contact: service4all@act.buaa.edu.cn
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 3.0 of the License, or any later version. 
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details. 
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
*/
package cn.edu.buaa.act.service4all.core.component.messaging;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;
import javax.xml.namespace.QName;
import cn.edu.buaa.act.service4all.core.component.messaging.MessageFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.id.IdGenerator;
import org.apache.servicemix.jbi.framework.ComponentContextImpl;
import org.apache.servicemix.jbi.messaging.MessageExchangeFactoryImpl;

/**
 * This is the factory class which is responsible for creating the message
 * exchange
 * 
 * 
 * 
 */
public class MessageFactory {

	private final Log logger = LogFactory.getLog(MessageFactory.class);
	private IdGenerator idgenerator;
	private MessageExchangeFactoryImpl factory;

	public MessageFactory(ComponentContextImpl context) {
		this.idgenerator = new IdGenerator();
		this.factory = new MessageExchangeFactoryImpl(this.idgenerator,
				new AtomicBoolean(false));
		factory.setContext(context);
	}

	public MessageFactory(IdGenerator idgenerator, ComponentContextImpl context) {
		this.idgenerator = idgenerator;
		this.factory = new MessageExchangeFactoryImpl(this.idgenerator,
				new AtomicBoolean(false));
		factory.setContext(context);
	}

	public MessageExchange createExchange(URI pattern)
			throws MessagingException {
		try {
			return factory.createExchange(pattern);
		} catch (MessagingException e) {
			logger.warn(e.getMessage()
					+ ", but return a default pattern: InOut");
			return createInOutExchange();
		}
	}

	public InOut createInOutExchange() throws MessagingException {
		return factory.createInOutExchange();
	}

	public InOut createInOutExchange(QName serviceName, QName opName,
			QName interfaceName, String endpoint) throws MessagingException {
		InOut inout = factory.createInOutExchange();
		inout.setService(serviceName);
		inout.setOperation(opName);
		inout.setInterfaceName(interfaceName);
		return inout;
	}
}
