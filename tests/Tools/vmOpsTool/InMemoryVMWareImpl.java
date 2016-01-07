import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryVMWareImpl implements IVMWare {

    private Map<String, List<String>> vmSnapshotInfo = new HashMap<String, List<String>>();
    private Map<String, String> vmActiveSnapshot = new HashMap<String, String>();
    private List<String> snapshotList = new ArrayList<String>();
    private String activeSnapshot = "Snapshot2";

    public InMemoryVMWareImpl() {
        snapshotList.add("Snapshot1");
        snapshotList.add("Snapshot2");

        vmSnapshotInfo.put("win2012r2", snapshotList);
        vmSnapshotInfo.put("poweredoffvm", snapshotList);
        vmSnapshotInfo.put("win10", snapshotList);
        vmSnapshotInfo.put("ubuntuvm", snapshotList);
        vmSnapshotInfo.put("win8", snapshotList);
        vmSnapshotInfo.put("win7", snapshotList);
        vmSnapshotInfo.put("vm1", snapshotList);
        vmSnapshotInfo.put("vm2", snapshotList);

        vmActiveSnapshot.put("win2012r2", activeSnapshot);
        vmActiveSnapshot.put("poweredoffvm", activeSnapshot);
        vmActiveSnapshot.put("win10", activeSnapshot);
        vmActiveSnapshot.put("ubuntuvm", activeSnapshot);
        vmActiveSnapshot.put("win8", activeSnapshot);
        vmActiveSnapshot.put("win7", activeSnapshot);
        vmActiveSnapshot.put("vm1", activeSnapshot);
        vmActiveSnapshot.put("vm2", activeSnapshot);
    }

    public void createSnapshot(String vmName, String snapshotName, boolean saveVMMemory, boolean quiesceFs,
            String description, ConnectionData connData) throws Exception {
        vmName = vmName.toLowerCase();
        if (vmSnapshotInfo.containsKey(vmName)) {
            List<String> vmCpList = vmSnapshotInfo.get(vmName);
            vmCpList.add(snapshotName);
            vmSnapshotInfo.put(vmName, vmCpList);
            vmActiveSnapshot.put(vmName, snapshotName);
        } else {
            throw new Exception("VM not found.");
        }
    }

    public void restoreSnapshot(String vmName, String snapshotName, ConnectionData connData) throws Exception {
        List<String> cpList = null;

        vmName = vmName.toLowerCase();
        if (vmSnapshotInfo.containsKey(vmName)) {
            cpList = vmSnapshotInfo.get(vmName);
            if (!cpList.contains(snapshotName)) {
                System.out.println("Snapshot does not exist: " + snapshotName);
                throw new Exception("Snapshot does not exist: " + snapshotName);
            }
            vmActiveSnapshot.put(vmName, snapshotName);

        } else {
            throw new Exception("VM not found.");
        }
        return;
    }

    public void deleteSnapshot(String vmName, String snapshotName, ConnectionData connData) throws Exception {
        vmName = vmName.toLowerCase();
        if (vmSnapshotInfo.containsKey(vmName)) {
            List<String> vmCpList = vmSnapshotInfo.get(vmName);
            if (vmCpList.contains(snapshotName)) {
                vmCpList.remove(snapshotName);
                vmSnapshotInfo.put(vmName, vmCpList);
                vmActiveSnapshot.put(vmName, vmCpList.get(vmCpList.size() - 1));
            } else {
                throw new Exception("Snapshot not found !!");
            }
        } else {
            throw new Exception("VM not found.");
        }
    }

    public void connect(ConnectionData connData) throws Exception {
        if (connData.password.equals("InvalidPassword")) {
            throw new Exception();
        }
    }

    public String getCurrentSnapshot(String vmName, ConnectionData connData) throws Exception {

        String currentSnapshotName = null;

        vmName = vmName.toLowerCase();
        if (vmSnapshotInfo.containsKey(vmName)) {
            currentSnapshotName = vmActiveSnapshot.get(vmName);
        } else {
            throw new Exception("VM not found.");
        }

        return currentSnapshotName;
    }

    public boolean snapshotExists(String vmName, String snapshotName, ConnectionData connData) throws Exception {

        vmName = vmName.toLowerCase();
        if (vmSnapshotInfo.containsKey(vmName)) {
            return vmSnapshotInfo.containsValue(snapshotName);
        } else {
            throw new Exception("VM not found.");
        }
    }
}
