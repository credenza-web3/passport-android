package com.credenza.credenzapassport.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
public class MetadataMembershipContract extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b50610632806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80630d8e6e2c1461005c578063502ccb091461007a57806369b3d09c1461008f578063b3d68181146100a2578063ea9d669b146100b5575b600080fd5b6100646100ff565b6040516100719190610536565b60405180910390f35b61008d610088366004610459565b61011f565b005b61006461009d366004610427565b6101ab565b61008d6100b0366004610406565b6102fd565b6100ef6100c3366004610427565b6001600160a01b0391821660009081526020818152604080832093909416825291909152205460ff1690565b6040519015158152602001610071565b60606040518060600160405280602281526020016105db60229139905090565b336000818152602081815260408083206001600160a01b038716808552908352818420805460ff19166001908117909155948452938252808320938352928152919020825161017092840190610351565b506040516001600160a01b0383169033907fce83807ca74d689869c29adbce77172c8a762f90e26450adebe0a2ec29c80c2b90600090a35050565b60405163ea9d669b60e01b81526001600160a01b03808416600483015282166024820152606090309063ea9d669b9060440160206040518083038186803b1580156101f557600080fd5b505afa158015610209573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525081019061022d9190610516565b61024657506040805160208101909152600081526102f7565b6001600160a01b038084166000908152600160209081526040808320938616835292905220805461027690610589565b80601f01602080910402602001604051908101604052809291908181526020018280546102a290610589565b80156102ef5780601f106102c4576101008083540402835291602001916102ef565b820191906000526020600020905b8154815290600101906020018083116102d257829003601f168201915b505050505090505b92915050565b336000818152602081815260408083206001600160a01b0386168085529252808320805460ff19169055519092917f153a07e9ccc07353bbcd9c48710cd8ff1be4828e16589d8aa5f708d24ba1954691a350565b82805461035d90610589565b90600052602060002090601f01602090048101928261037f57600085556103c5565b82601f1061039857805160ff19168380011785556103c5565b828001600101855582156103c5579182015b828111156103c55782518255916020019190600101906103aa565b506103d19291506103d5565b5090565b5b808211156103d157600081556001016103d6565b80356001600160a01b038116811461040157600080fd5b919050565b600060208284031215610417578081fd5b610420826103ea565b9392505050565b60008060408385031215610439578081fd5b610442836103ea565b9150610450602084016103ea565b90509250929050565b6000806040838503121561046b578182fd5b610474836103ea565b9150602083013567ffffffffffffffff80821115610490578283fd5b818501915085601f8301126104a3578283fd5b8135818111156104b5576104b56105c4565b604051601f8201601f19908116603f011681019083821181831017156104dd576104dd6105c4565b816040528281528860208487010111156104f5578586fd5b82602086016020830137856020848301015280955050505050509250929050565b600060208284031215610527578081fd5b81518015158114610420578182fd5b6000602080835283518082850152825b8181101561056257858101830151858201604001528201610546565b818111156105735783604083870101525b50601f01601f1916929092016040019392505050565b600181811c9082168061059d57607f821691505b602082108114156105be57634e487b7160e01b600052602260045260246000fd5b50919050565b634e487b7160e01b600052604160045260246000fdfe4d657461646174614d656d62657273686970436f6e7472616374207620302e302e31a2646970667358221220736e945d81cdbb53729eff4572d346349f2a9a9eca45470a974959772e91486964736f6c63430008040033";

    public static final String FUNC_ADDMEMBERSHIP = "addMembership";

    public static final String FUNC_CONFIRMMEMBERSHIP = "confirmMembership";

    public static final String FUNC_GETMEMBERSHIPMETADATA = "getMembershipMetadata";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_REMOVEMEMBERSHIP = "removeMembership";

    public static final Event MEMBERSHIPADDED_EVENT = new Event("MembershipAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event MEMBERSHIPREMOVED_EVENT = new Event("MembershipRemoved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected MetadataMembershipContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MetadataMembershipContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MetadataMembershipContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MetadataMembershipContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<MembershipAddedEventResponse> getMembershipAddedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERSHIPADDED_EVENT, transactionReceipt);
        ArrayList<MembershipAddedEventResponse> responses = new ArrayList<MembershipAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MembershipAddedEventResponse typedResponse = new MembershipAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.target = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MembershipAddedEventResponse> membershipAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MembershipAddedEventResponse>() {
            @Override
            public MembershipAddedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MEMBERSHIPADDED_EVENT, log);
                MembershipAddedEventResponse typedResponse = new MembershipAddedEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.target = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MembershipAddedEventResponse> membershipAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERSHIPADDED_EVENT));
        return membershipAddedEventFlowable(filter);
    }

    public static List<MembershipRemovedEventResponse> getMembershipRemovedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MEMBERSHIPREMOVED_EVENT, transactionReceipt);
        ArrayList<MembershipRemovedEventResponse> responses = new ArrayList<MembershipRemovedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MembershipRemovedEventResponse typedResponse = new MembershipRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.target = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MembershipRemovedEventResponse> membershipRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MembershipRemovedEventResponse>() {
            @Override
            public MembershipRemovedEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MEMBERSHIPREMOVED_EVENT, log);
                MembershipRemovedEventResponse typedResponse = new MembershipRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.target = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MembershipRemovedEventResponse> membershipRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MEMBERSHIPREMOVED_EVENT));
        return membershipRemovedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addMembership(String customerAddress, String metadata) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDMEMBERSHIP,
                Arrays.<Type>asList(new Address(160, customerAddress),
                        new Utf8String(metadata)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> confirmMembership(String publisherAddress, String customerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONFIRMMEMBERSHIP,
                Arrays.<Type>asList(new Address(160, publisherAddress),
                        new Address(160, customerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> getMembershipMetadata(String publisherAddress, String customerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETMEMBERSHIPMETADATA,
                Arrays.<Type>asList(new Address(160, publisherAddress),
                        new Address(160, customerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getVersion() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETVERSION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeMembership(String customerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEMEMBERSHIP,
                Arrays.<Type>asList(new Address(160, customerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static MetadataMembershipContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MetadataMembershipContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MetadataMembershipContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MetadataMembershipContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MetadataMembershipContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MetadataMembershipContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MetadataMembershipContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MetadataMembershipContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MetadataMembershipContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MetadataMembershipContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MetadataMembershipContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MetadataMembershipContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<MetadataMembershipContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MetadataMembershipContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MetadataMembershipContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MetadataMembershipContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class MembershipAddedEventResponse extends BaseEventResponse {
        public String owner;

        public String target;
    }

    public static class MembershipRemovedEventResponse extends BaseEventResponse {
        public String owner;

        public String target;
    }
}
