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
        Crdt crdt = crdtMap.getOrDefault(docId,null);
        if(crdt == null){
            return null;
        }
        synchronized (crdt){
            return crdt;
        }
    }
    public void createCrdt(Long docId){
        if(crdtMap.containsKey(docId)){
            return;
        }
        Crdt crdt = new Crdt();
        crdtMap.put(docId,crdt);
        synchronized (crdt) {
            byte[] crdtContent = docRepository.getDocById(docId).get().getContent();
            crdt.InitCrdt(crdtContent);
            crdtMap.put(docId, crdt);
        }
    }

    public synchronized void saveAndDeleteCrdt(Long docId){
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
