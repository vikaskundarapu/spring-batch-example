package com.vikas.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.vikas.springboot.model.Leader;

public class LeaderItemProcessor implements ItemProcessor<Leader, Leader> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LeaderItemProcessor.class);

	@Override
	public Leader process(Leader leader) throws Exception {

		final String firstName = leader.getFirstName().toUpperCase();
		final String lastName = leader.getLastName().toUpperCase();
		final Leader transformedPerson = new Leader(firstName, lastName);
		LOGGER.info("Converting (" + leader + ") into (" + transformedPerson + ")");

		return transformedPerson;
	}

}
