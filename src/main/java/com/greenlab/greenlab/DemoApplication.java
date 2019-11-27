package com.greenlab.greenlab;

import com.greenlab.greenlab.labEquip.equipment.equipmentData.equipmentData.EquipmentDataRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipment;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolder;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentFolderRepository;
import com.greenlab.greenlab.labEquip.equipment.equipmentData.userEquipment.UserEquipmentRepository;
import com.greenlab.greenlab.labEquip.framework.imageBlob.ImageBlobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.LinkedList;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Autowired
	private UserEquipmentRepository userEquipmentRepository;

	@Autowired
	private UserEquipmentFolderRepository userEquipmentFolderRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private EquipmentDataRepository equipmentDataRepository;

	@Autowired
	private ImageBlobRepository imageBlobRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run(String... args) throws Exception {

		createOneUser();


	}

	public void createOneUser(){

		String defaultUser = "weixin.tang@stonybrook.edu";

		UserEquipment userEquipment =  userEquipmentRepository.findByUserId(  defaultUser  );

		if( userEquipment == null ){


			LinkedList<String> folderIds = new LinkedList<>();
			UserEquipmentFolder userEquipmentFolder;

			userEquipmentFolder = new UserEquipmentFolder();
			userEquipmentFolder.setType("all");
			userEquipmentFolder.setOwner("weixin.tang@stonybrook.edu");
			folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());
			userEquipmentFolder = new UserEquipmentFolder();
			userEquipmentFolder.setType("share");
			userEquipmentFolder.setOwner("weixin.tang@stonybrook.edu");
			folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());
			userEquipmentFolder = new UserEquipmentFolder();
			userEquipmentFolder.setType("favourite");
			userEquipmentFolder.setOwner("weixin.tang@stonybrook.edu");
			folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());


			userEquipmentFolder = new UserEquipmentFolder();
			userEquipmentFolder.setType("recent");
			userEquipmentFolder.setOwner("weixin.tang@stonybrook.edu");
			folderIds.add(userEquipmentFolderRepository.save(userEquipmentFolder).getUserFolderId());
			userEquipment = new UserEquipment();
			userEquipment.setUserId("weixin.tang@stonybrook.edu");
			userEquipment.setFolderIds(folderIds);
			userEquipmentRepository.save(userEquipment);

		}else{

		}




	}

}
