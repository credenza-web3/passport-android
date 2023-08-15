package com.credenza.credenzapassport.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class MembershipContract extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b506004361061004c5760003560e01c80630d8e6e2c14610051578063b3d6818114610099578063c5db808b146100d5578063ea9d669b14610112575b600080fd5b604080518082018252601a81527f4d656d62657273686970436f6e7472616374207620302e302e310000000000006020820152905161009091906101cb565b60405180910390f35b6100d36100a7366004610178565b336000908152602081815260408083206001600160a01b0394909416835292905220805460ff19169055565b005b6100d36100e3366004610178565b336000908152602081815260408083206001600160a01b0394909416835292905220805460ff19166001179055565b61014c610120366004610199565b6001600160a01b0391821660009081526020818152604080832093909416825291909152205460ff1690565b6040519015158152602001610090565b80356001600160a01b038116811461017357600080fd5b919050565b600060208284031215610189578081fd5b6101928261015c565b9392505050565b600080604083850312156101ab578081fd5b6101b48361015c565b91506101c26020840161015c565b90509250929050565b6000602080835283518082850152825b818110156101f7578581018301518582016040015282016101db565b818111156102085783604083870101525b50601f01601f191692909201604001939250505056fea26469706673582212207ec897da10c87a80f71d77845ee8b23acc4467619a6903a51fb26c2ed8ccf0f564736f6c63430008040033";

    public static final String FUNC_ADDMEMBERSHIP = "addMembership";

    public static final String FUNC_CONFIRMMEMBERSHIP = "confirmMembership";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_REMOVEMEMBERSHIP = "removeMembership";

    @Deprecated
    protected MembershipContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MembershipContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MembershipContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MembershipContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addMembership(String customerAddress) {
        final Function function = new Function(
                FUNC_ADDMEMBERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, customerAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> confirmMembership(String publisherAddress, String customerAddress) {
        final Function function = new Function(FUNC_CONFIRMMEMBERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, publisherAddress), 
                new org.web3j.abi.datatypes.Address(160, customerAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> getVersion() {
        final Function function = new Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeMembership(String customerAddress) {
        final Function function = new Function(
                FUNC_REMOVEMEMBERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, customerAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static MembershipContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MembershipContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MembershipContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MembershipContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MembershipContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MembershipContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MembershipContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MembershipContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MembershipContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MembershipContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MembershipContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MembershipContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<MembershipContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MembershipContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MembershipContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MembershipContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
