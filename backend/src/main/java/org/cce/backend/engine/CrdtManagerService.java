package org.cce.backend.engine;

import org.cce.backend.repository.DocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class CrdtManagerService {
    @Autowired
    private DocRepository docRepository;
    private ConcurrentHashMap<Long, Crdt> crdtMap = new ConcurrentHashMap<>();
    public Crdt getCrdt(Long docId){
        return crdtMap.getOrDefault(docId,null);
    }
    public void createCrdt(Long docId){
        if(crdtMap.containsKey(docId)){
            return;
        }
        byte[] crdtContent = docRepository.getDocById(docId).get().getContent();
        Crdt crdt = new Crdt(crdtContent);
        crdtMap.put(docId, crdt);
    }

    public void saveAndDeleteCrdt(Long docId){
        if(!crdtMap.containsKey(docId)){
            return;
        }
        docRepository.findById(docId).ifPresent(doc -> {
            doc.setContent(crdtMap.get(docId).getSerializedCrdt());
            docRepository.save(doc);
        });
        deleteCrdt(docId);
    }
    private void deleteCrdt(Long docId){
        crdtMap.remove(docId);
    }
}
