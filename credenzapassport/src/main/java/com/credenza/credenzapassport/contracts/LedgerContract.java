package com.credenza.credenzapassport.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class LedgerContract extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b5060405161151e38038061151e83398101604081905261002f9161016a565b61003833610066565b600280546001600160a01b039092166001600160a01b03199283161790556003805490911633179055610198565b6001541580159061008757503360009081526020819052604090205460ff16155b156100e35760405162461bcd60e51b815260206004820152602260248201527f4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f604482015261371760f11b606482015260840160405180910390fd5b6001600160a01b03811660009081526020819052604090205460ff16156101075750565b6001600160a01b03166000818152602081905260408120805460ff191660019081179091558054808201825591527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60180546001600160a01b0319169091179055565b60006020828403121561017b578081fd5b81516001600160a01b0381168114610191578182fd5b9392505050565b611377806101a76000396000f3fe608060405234801561001057600080fd5b50600436106101375760003560e01c80637065cb48116100b8578063ad102b841161007c578063ad102b84146102ec578063c75305bc146102ff578063d4f1205c14610312578063e1e610e114610325578063ea0f40fe14610338578063ee28f5011461036157600080fd5b80637065cb481461027e57806372dd52e31461029157806391825dca146102bc578063a0e67e2b146102c4578063a99752c4146102d957600080fd5b80632f54bf6e116100ff5780632f54bf6e146101d257806334e0ee2e146101f55780635d0fd6c11461022c5780635e9b45e11461023f578063668ff4271461026b57600080fd5b80630d8e6e2c1461013c57806315650cac14610184578063173825d91461019957806327a920dc146101ac57806329dd5120146101bf575b600080fd5b604080518082018252601781527f4c6f79616c7479436f6e7472616374207620302e332e300000000000000000006020820152905161017b91906110cb565b60405180910390f35b610197610192366004610ef5565b610381565b005b6101976101a7366004610eab565b6104b9565b6101976101ba366004610eab565b610694565b6101976101cd366004610f27565b6106db565b6101e56101e0366004610eab565b6107aa565b604051901515815260200161017b565b61021e610203366004610eab565b6001600160a01b031660009081526005602052604090205490565b60405190815260200161017b565b6101e561023a366004610eab565b6107c8565b6101e561024d366004610eab565b6001600160a01b031660009081526004602052604090205460ff1690565b610197610279366004610ef5565b6107fc565b61019761028c366004610eab565b6109b0565b6002546102a4906001600160a01b031681565b6040516001600160a01b03909116815260200161017b565b6102a4610a58565b6102cc610a8f565b60405161017b9190611025565b6101976102e7366004610eab565b610af1565b6101976102fa366004610ecc565b610b3a565b61019761030d366004610eab565b610bfe565b610197610320366004610eab565b610c44565b610197610333366004610ecc565b610c8b565b61021e610346366004610eab565b6001600160a01b031660009081526006602052604090205490565b61037461036f366004610eab565b610dfc565b60405161017b9190611072565b61038a336107c8565b6103af5760405162461bcd60e51b81526004016103a690611160565b60405180910390fd5b600082136103cf5760405162461bcd60e51b81526004016103a6906111ad565b6001600160a01b038316600090815260056020526040812080548492906103f7908490611249565b90915550506001600160a01b03831660009081526006602052604081208054849290610424908490611249565b90915550506001600160a01b0383166000818152600760209081526040808320815160608101835242815280840187815281840189815283546001818101865594885295872092516003909602909201948555519184019190915551600290920191909155518392859290917f67839115ecfc56e55723f233dbb40ca67429025c19e220e84a0de77357dce0fc9190a4505050565b6104c2336107aa565b6104de5760405162461bcd60e51b81526004016103a69061111e565b6104e7816107aa565b6105335760405162461bcd60e51b815260206004820152601e60248201527f5461726765742061646472657373206973206e6f7420616e206f776e6572000060448201526064016103a6565b6001600160a01b0381166000908152602081905260408120805460ff191690555b60015481101561069057816001600160a01b03166001828154811061058957634e487b7160e01b600052603260045260246000fd5b6000918252602090912001546001600160a01b0316141561067e57600180546105b39082906112c9565b815481106105d157634e487b7160e01b600052603260045260246000fd5b600091825260209091200154600180546001600160a01b03909216918390811061060b57634e487b7160e01b600052603260045260246000fd5b9060005260206000200160006101000a8154816001600160a01b0302191690836001600160a01b03160217905550600180548061065857634e487b7160e01b600052603160045260246000fd5b600082815260209020810160001990810180546001600160a01b03191690550190555050565b80610688816112e0565b915050610554565b5050565b61069d336107c8565b6106b95760405162461bcd60e51b81526004016103a690611160565b600380546001600160a01b0319166001600160a01b0392909216919091179055565b6106e4336107c8565b6107005760405162461bcd60e51b81526004016103a690611160565b60005b81518110156106905761079882828151811061072f57634e487b7160e01b600052603260045260246000fd5b60200260200101516000015183838151811061075b57634e487b7160e01b600052603260045260246000fd5b60200260200101516020015184848151811061078757634e487b7160e01b600052603260045260246000fd5b602002602001015160400151610381565b806107a2816112e0565b915050610703565b6001600160a01b031660009081526020819052604090205460ff1690565b60006107d3826107aa565b806107f657506001600160a01b03821660009081526004602052604090205460ff165b92915050565b6001600160a01b0383163314806108175750610817336107c8565b6108635760405162461bcd60e51b815260206004820152601f60248201527f596f752063616e2072656465656d206f6e6c7920796f757220706f696e74730060448201526064016103a6565b600082136108835760405162461bcd60e51b81526004016103a6906111ad565b6001600160a01b0383166000908152600560205260409020548213156108df5760405162461bcd60e51b81526020600482015260116024820152704e6f7420656e6f75676820706f696e747360781b60448201526064016103a6565b6001600160a01b0383166000908152600560205260408120805484929061090790849061128a565b90915550506001600160a01b0383166000908152600760209081526040918290208251606081018452428152918201849052918101610945856112fb565b90528154600181810184556000938452602080852084516003909402019283558301519082015560409182015160029091015551829184916001600160a01b038716917fee3b1ba81f87423fecc5e13b69a00e5be4d7e9970fb24498122085bbef9b973891a4505050565b600154158015906109c757506109c5336107aa565b155b156109e45760405162461bcd60e51b81526004016103a69061111e565b6109ed816107aa565b156109f55750565b6001600160a01b03166000818152602081905260408120805460ff191660019081179091558054808201825591527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60180546001600160a01b0319169091179055565b6000610a63336107c8565b610a7f5760405162461bcd60e51b81526004016103a690611160565b506003546001600160a01b031690565b60606001805480602002602001604051908101604052809291908181526020018280548015610ae757602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610ac9575b5050505050905090565b610afa336107aa565b610b165760405162461bcd60e51b81526004016103a69061111e565b6001600160a01b03166000908152600460205260409020805460ff19166001179055565b60008113610b5a5760405162461bcd60e51b81526004016103a6906111ad565b610b66828260016107fc565b6002546003546040516323b872dd60e01b81526001600160a01b03918216600482015284821660248201526044810184905291169081906323b872dd90606401602060405180830381600087803b158015610bc057600080fd5b505af1158015610bd4573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610bf89190611005565b50505050565b610c07336107aa565b610c235760405162461bcd60e51b81526004016103a69061111e565b6001600160a01b03166000908152600460205260409020805460ff19169055565b610c4d336107c8565b610c695760405162461bcd60e51b81526004016103a690611160565b600280546001600160a01b0319166001600160a01b0392909216919091179055565b610c94336107c8565b610cb05760405162461bcd60e51b81526004016103a690611160565b60008113610cd05760405162461bcd60e51b81526004016103a6906111ad565b6001600160a01b038216600090815260056020526040902054811315610d2c5760405162461bcd60e51b81526020600482015260116024820152704e6f7420656e6f75676820706f696e747360781b60448201526064016103a6565b6001600160a01b03821660009081526005602052604081208054839290610d5490849061128a565b90915550506001600160a01b03821660009081526006602052604081208054839290610d8190849061128a565b90915550506001600160a01b0382166000908152600760209081526040918290208251606081018452428152600292810192909252918101610dc2846112fb565b9052815460018181018455600093845260209384902083516003909302019182559282015192810192909255604001516002909101555050565b6001600160a01b0381166000908152600760209081526040808320805482518185028101850190935280835260609492939192909184015b82821015610e845783829060005260206000209060030201604051806060016040529081600082015481526020016001820154815260200160028201548152505081526020019060010190610e34565b505050509050919050565b80356001600160a01b0381168114610ea657600080fd5b919050565b600060208284031215610ebc578081fd5b610ec582610e8f565b9392505050565b60008060408385031215610ede578081fd5b610ee783610e8f565b946020939093013593505050565b600080600060608486031215610f09578081fd5b610f1284610e8f565b95602085013595506040909401359392505050565b60006020808385031215610f39578182fd5b823567ffffffffffffffff80821115610f50578384fd5b818501915085601f830112610f63578384fd5b813581811115610f7557610f7561132b565b610f83848260051b01611218565b81815284810192508385016060808402860187018a1015610fa2578788fd5b8795505b83861015610ff75780828b031215610fbc578788fd5b610fc46111ef565b610fcd83610e8f565b81528288013588820152604080840135908201528552600195909501949386019390810190610fa6565b509098975050505050505050565b600060208284031215611016578081fd5b81518015158114610ec5578182fd5b6020808252825182820181905260009190848201906040850190845b818110156110665783516001600160a01b031683529284019291840191600101611041565b50909695505050505050565b602080825282518282018190526000919060409081850190868401855b828110156110be578151805185528681015187860152850151858501526060909301929085019060010161108f565b5091979650505050505050565b6000602080835283518082850152825b818110156110f7578581018301518582016040015282016110db565b818111156111085783604083870101525b50601f01601f1916929092016040019392505050565b60208082526022908201527f4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f604082015261371760f11b606082015260800190565b6020808252602d908201527f4f6e6c79206f776e6572206f7220706172746e65722063616e2063616c6c207460408201526c3434b990333ab731ba34b7b71760991b606082015260800190565b60208082526022908201527f416d6f756e742073686f756c642062652067726561746572207468616e207a65604082015261726f60f01b606082015260800190565b6040516060810167ffffffffffffffff811182821017156112125761121261132b565b60405290565b604051601f8201601f1916810167ffffffffffffffff811182821017156112415761124161132b565b604052919050565b600080821280156001600160ff1b038490038513161561126b5761126b611315565b600160ff1b839003841281161561128457611284611315565b50500190565b60008083128015600160ff1b8501841216156112a8576112a8611315565b6001600160ff1b03840183138116156112c3576112c3611315565b50500390565b6000828210156112db576112db611315565b500390565b60006000198214156112f4576112f4611315565b5060010190565b6000600160ff1b82141561131157611311611315565b0390565b634e487b7160e01b600052601160045260246000fd5b634e487b7160e01b600052604160045260246000fdfea2646970667358221220f70b7e91a21817d9902fe996bbe252791c6335ce3ef3ca1ac3b8746dd6f3f51164736f6c63430008040033";

    public static final String FUNC_ADDAUTHORIZEDPARTNER = "addAuthorizedPartner";

    public static final String FUNC_ADDBATCHPOINTS = "addBatchPoints";

    public static final String FUNC_ADDOWNER = "addOwner";

    public static final String FUNC_ADDPOINTS = "addPoints";

    public static final String FUNC_CHECKLIFETIMEPOINTS = "checkLifetimePoints";

    public static final String FUNC_CHECKPOINTS = "checkPoints";

    public static final String FUNC_COINCONTRACT = "coinContract";

    public static final String FUNC_CONVERTPOINTSTOCOINS = "convertPointsToCoins";

    public static final String FUNC_FORFEITPOINTS = "forfeitPoints";

    public static final String FUNC_GETCOINTRANSFERSOURCEADDRESS = "getCoinTransferSourceAddress";

    public static final String FUNC_GETOWNERS = "getOwners";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_ISAUTHORIZEDPARTNER = "isAuthorizedPartner";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_ISPARTNEROROWNER = "isPartnerOrOwner";

    public static final String FUNC_REDEEMPOINTS = "redeemPoints";

    public static final String FUNC_REMOVEAUTHORIZEDPARTNER = "removeAuthorizedPartner";

    public static final String FUNC_REMOVEOWNER = "removeOwner";

    public static final String FUNC_RETRIEVELOGS = "retrieveLogs";

    public static final String FUNC_SETCOINCONTRACTADDRESS = "setCoinContractAddress";

    public static final String FUNC_SETCOINTRANSFERSOURCEADDRESS = "setCoinTransferSourceAddress";

    public static final Event ADDPOINTS_EVENT = new Event("AddPoints", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Int256>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    public static final Event REDEEMPOINTS_EVENT = new Event("RedeemPoints", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Int256>(true) {}, new TypeReference<Uint256>(true) {}));
    ;

    @Deprecated
    protected LedgerContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LedgerContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LedgerContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LedgerContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AddPointsEventResponse> getAddPointsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADDPOINTS_EVENT, transactionReceipt);
        ArrayList<AddPointsEventResponse> responses = new ArrayList<AddPointsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AddPointsEventResponse typedResponse = new AddPointsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddPointsEventResponse> addPointsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AddPointsEventResponse>() {
            @Override
            public AddPointsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADDPOINTS_EVENT, log);
                AddPointsEventResponse typedResponse = new AddPointsEventResponse();
                typedResponse.log = log;
                typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddPointsEventResponse> addPointsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDPOINTS_EVENT));
        return addPointsEventFlowable(filter);
    }

    public static List<RedeemPointsEventResponse> getRedeemPointsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REDEEMPOINTS_EVENT, transactionReceipt);
        ArrayList<RedeemPointsEventResponse> responses = new ArrayList<RedeemPointsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            RedeemPointsEventResponse typedResponse = new RedeemPointsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RedeemPointsEventResponse> redeemPointsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RedeemPointsEventResponse>() {
            @Override
            public RedeemPointsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(REDEEMPOINTS_EVENT, log);
                RedeemPointsEventResponse typedResponse = new RedeemPointsEventResponse();
                typedResponse.log = log;
                typedResponse.recipient = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.eventId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RedeemPointsEventResponse> redeemPointsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REDEEMPOINTS_EVENT));
        return redeemPointsEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addAuthorizedPartner(String partnerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDAUTHORIZEDPARTNER, 
                Arrays.<Type>asList(new Address(160, partnerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addBatchPoints(List<AddPointsBatchItem> items) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDBATCHPOINTS, 
                Arrays.<Type>asList(new DynamicArray<AddPointsBatchItem>(AddPointsBatchItem.class, items)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addOwner(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDOWNER, 
                Arrays.<Type>asList(new Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addPoints(String recipient, BigInteger amount, BigInteger eventId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDPOINTS, 
                Arrays.<Type>asList(new Address(160, recipient),
                new Int256(amount),
                new Uint256(eventId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> checkLifetimePoints(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKLIFETIMEPOINTS, 
                Arrays.<Type>asList(new Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> checkPoints(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKPOINTS, 
                Arrays.<Type>asList(new Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> coinContract() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COINCONTRACT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> convertPointsToCoins(String recipient, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONVERTPOINTSTOCOINS, 
                Arrays.<Type>asList(new Address(160, recipient),
                new Int256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> forfeitPoints(String recipient, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FORFEITPOINTS, 
                Arrays.<Type>asList(new Address(160, recipient),
                new Int256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getCoinTransferSourceAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETCOINTRANSFERSOURCEADDRESS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getOwners() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETOWNERS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getVersion() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isAuthorizedPartner(String partnerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISAUTHORIZEDPARTNER, 
                Arrays.<Type>asList(new Address(160, partnerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isOwner(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(new Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPartnerOrOwner(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISPARTNEROROWNER, 
                Arrays.<Type>asList(new Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> redeemPoints(String recipient, BigInteger amount, BigInteger eventId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REDEEMPOINTS, 
                Arrays.<Type>asList(new Address(160, recipient),
                new Int256(amount),
                new Uint256(eventId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAuthorizedPartner(String partnerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEAUTHORIZEDPARTNER, 
                Arrays.<Type>asList(new Address(160, partnerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeOwner(String owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEOWNER, 
                Arrays.<Type>asList(new Address(160, owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> retrieveLogs(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RETRIEVELOGS, 
                Arrays.<Type>asList(new Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<PointsLogItem>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> setCoinContractAddress(String coinContractAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOINCONTRACTADDRESS, 
                Arrays.<Type>asList(new Address(160, coinContractAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCoinTransferSourceAddress(String newAddr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOINTRANSFERSOURCEADDRESS, 
                Arrays.<Type>asList(new Address(160, newAddr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static LedgerContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LedgerContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LedgerContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LedgerContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LedgerContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LedgerContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LedgerContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LedgerContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LedgerContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String coinContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, coinContractAddress)));
        return deployRemoteCall(LedgerContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<LedgerContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String coinContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, coinContractAddress)));
        return deployRemoteCall(LedgerContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LedgerContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String coinContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, coinContractAddress)));
        return deployRemoteCall(LedgerContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LedgerContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String coinContractAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(160, coinContractAddress)));
        return deployRemoteCall(LedgerContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AddPointsBatchItem extends StaticStruct {
        public String recipient;

        public BigInteger amount;

        public BigInteger eventId;

        public AddPointsBatchItem(String recipient, BigInteger amount, BigInteger eventId) {
            super(new Address(160, recipient),
                    new Int256(amount),
                    new Uint256(eventId));
            this.recipient = recipient;
            this.amount = amount;
            this.eventId = eventId;
        }

        public AddPointsBatchItem(Address recipient, Int256 amount, Uint256 eventId) {
            super(recipient, amount, eventId);
            this.recipient = recipient.getValue();
            this.amount = amount.getValue();
            this.eventId = eventId.getValue();
        }
    }

    public static class PointsLogItem extends StaticStruct {
        public BigInteger timestamp;

        public BigInteger eventId;

        public BigInteger pointsAmount;

        public PointsLogItem(BigInteger timestamp, BigInteger eventId, BigInteger pointsAmount) {
            super(new Uint256(timestamp),
                    new Uint256(eventId),
                    new Int256(pointsAmount));
            this.timestamp = timestamp;
            this.eventId = eventId;
            this.pointsAmount = pointsAmount;
        }

        public PointsLogItem(Uint256 timestamp, Uint256 eventId, Int256 pointsAmount) {
            super(timestamp, eventId, pointsAmount);
            this.timestamp = timestamp.getValue();
            this.eventId = eventId.getValue();
            this.pointsAmount = pointsAmount.getValue();
        }
    }

    public static class AddPointsEventResponse extends BaseEventResponse {
        public String recipient;

        public BigInteger amount;

        public BigInteger eventId;
    }

    public static class RedeemPointsEventResponse extends BaseEventResponse {
        public String recipient;

        public BigInteger amount;

        public BigInteger eventId;
    }
}
