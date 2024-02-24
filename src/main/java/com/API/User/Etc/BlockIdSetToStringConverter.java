package com.API.User.Etc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.*;

@Converter
public class BlockIdSetToStringConverter implements AttributeConverter<Set<Integer>, String> {
	
    @Override
    public String convertToDatabaseColumn(Set<Integer> blockIds) {
    	
    	String column;
    	
        if (blockIds == null || blockIds.isEmpty()) {
        	column="";
        }
        else {
        	column = blockIds.stream()
        			.map(String::valueOf)
        			.collect(Collectors.joining(","));
        	}
        
        return column;
    }

    @Override
    public Set<Integer> convertToEntityAttribute(String dbData) {
    	
    	Set<Integer> blockId;
    	
        if (dbData == null || dbData.trim().isEmpty()) {
            blockId =  new HashSet<>();
        }
        else {
        	blockId = Arrays.stream(dbData.split(","))
                     .map(Integer::valueOf)
                     .collect(Collectors.toSet());
        }
        return blockId;
    }
    
}
