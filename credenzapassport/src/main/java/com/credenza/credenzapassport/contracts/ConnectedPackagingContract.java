package com.credenza.credenzapassport.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
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
public class ConnectedPackagingContract extends Contract {
    public static final String BINARY = "0x608060405234801561001057600080fd5b5061001a3361001f565b610123565b6001541580159061004057503360009081526020819052604090205460ff16155b1561009c5760405162461bcd60e51b815260206004820152602260248201527f4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f604482015261371760f11b606482015260840160405180910390fd5b6001600160a01b03811660009081526020819052604090205460ff16156100c05750565b6001600160a01b03166000818152602081905260408120805460ff191660019081179091558054808201825591527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60180546001600160a01b0319169091179055565b6109af806101326000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c806370076c151161006657806370076c15146101015780637065cb4814610114578063729f01fd14610127578063a0e67e2b1461013a578063ee68b6841461014f57600080fd5b806309836ff2146100985780630d8e6e2c146100ad578063173825d9146100cb5780632f54bf6e146100de575b600080fd5b6100ab6100a636600461079f565b61017a565b005b6100b56102c2565b6040516100c29190610854565b60405180910390f35b6100ab6100d9366004610743565b6102e2565b6100f16100ec366004610743565b6104b9565b60405190151581526020016100c2565b6100ab61010f36600461079f565b6104d7565b6100ab610122366004610743565b6104fc565b6100ab610135366004610764565b6105a4565b61014261060d565b6040516100c29190610807565b61016261015d366004610764565b61066f565b6040516001600160a01b0390911681526020016100c2565b610183336104b9565b6101a85760405162461bcd60e51b815260040161019f90610887565b60405180910390fd5b806001600160a01b03166002836040516101c291906107eb565b908152604051908190036020019020546001600160a01b031614156101e5575050565b60006001600160a01b031660028360405161020091906107eb565b908152604051908190036020019020546001600160a01b03161461027d5760405162461bcd60e51b815260206004820152602e60248201527f53657269616c206e756d6265722070726f766964656420616c7265616479206860448201526d30b99030b9b9b7b1b4b0ba34b7b760911b606482015260840161019f565b8060028360405161028e91906107eb565b90815260405190819003602001902080546001600160a01b03929092166001600160a01b03199092169190911790555b5050565b606060405180606001604052806022815260200161095860229139905090565b6102eb336104b9565b6103075760405162461bcd60e51b815260040161019f90610887565b610310816104b9565b61035c5760405162461bcd60e51b815260206004820152601e60248201527f5461726765742061646472657373206973206e6f7420616e206f776e65720000604482015260640161019f565b6001600160a01b0381166000908152602081905260408120805460ff191690555b6001548110156102be57816001600160a01b0316600182815481106103b257634e487b7160e01b600052603260045260246000fd5b6000918252602090912001546001600160a01b031614156104a757600180546103dc9082906108c9565b815481106103fa57634e487b7160e01b600052603260045260246000fd5b600091825260209091200154600180546001600160a01b03909216918390811061043457634e487b7160e01b600052603260045260246000fd5b9060005260206000200160006101000a8154816001600160a01b0302191690836001600160a01b03160217905550600180548061048157634e487b7160e01b600052603160045260246000fd5b600082815260209020810160001990810180546001600160a01b03191690550190555050565b806104b181610910565b91505061037d565b6001600160a01b031660009081526020819052604090205460ff1690565b6104e0336104b9565b61027d5760405162461bcd60e51b815260040161019f90610887565b600154158015906105135750610511336104b9565b155b156105305760405162461bcd60e51b815260040161019f90610887565b610539816104b9565b156105415750565b6001600160a01b03166000818152602081905260408120805460ff191660019081179091558054808201825591527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60180546001600160a01b0319169091179055565b6105ad336104b9565b6105c95760405162461bcd60e51b815260040161019f90610887565b60006002826040516105db91906107eb565b90815260405190819003602001902080546001600160a01b03929092166001600160a01b031990921691909117905550565b6060600180548060200260200160405190810160405280929190818152602001828054801561066557602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610647575b5050505050905090565b600060028260405161068191906107eb565b908152604051908190036020019020546001600160a01b031692915050565b80356001600160a01b03811681146106b757600080fd5b919050565b600082601f8301126106cc578081fd5b813567ffffffffffffffff808211156106e7576106e7610941565b604051601f8301601f19908116603f0116810190828211818310171561070f5761070f610941565b81604052838152866020858801011115610727578485fd5b8360208701602083013792830160200193909352509392505050565b600060208284031215610754578081fd5b61075d826106a0565b9392505050565b600060208284031215610775578081fd5b813567ffffffffffffffff81111561078b578182fd5b610797848285016106bc565b949350505050565b600080604083850312156107b1578081fd5b823567ffffffffffffffff8111156107c7578182fd5b6107d3858286016106bc565b9250506107e2602084016106a0565b90509250929050565b600082516107fd8184602087016108e0565b9190910192915050565b6020808252825182820181905260009190848201906040850190845b818110156108485783516001600160a01b031683529284019291840191600101610823565b50909695505050505050565b60208152600082518060208401526108738160408501602087016108e0565b601f01601f19169190910160400192915050565b60208082526022908201527f4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f604082015261371760f11b606082015260800190565b6000828210156108db576108db61092b565b500390565b60005b838110156108fb5781810151838201526020016108e3565b8381111561090a576000848401525b50505050565b60006000198214156109245761092461092b565b5060010190565b634e487b7160e01b600052601160045260246000fd5b634e487b7160e01b600052604160045260246000fdfe436f6e6e65637465645061636b6167696e67436f6e7472616374207620302e302e31a2646970667358221220123cca7e458f44f16ec541db8fd0c541cf54df2102cf13d5e4bd387ac8c498e964736f6c63430008040033";

    public static final String FUNC_ADDOWNER = "addOwner";

    public static final String FUNC_CLAIMCONNECTION = "claimConnection";

    public static final String FUNC_GETOWNERS = "getOwners";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_REMOVEOWNER = "removeOwner";

    public static final String FUNC_RETRIEVECONNECTION = "retrieveConnection";

    public static final String FUNC_REVOKECONNECTION = "revokeConnection";

    public static final String FUNC_TRANSFERCONNECTION = "transferConnection";

    @Deprecated
    protected ConnectedPackagingContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ConnectedPackagingContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ConnectedPackagingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ConnectedPackagingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addOwner(String newOwner) {
        final Function function = new Function(
                FUNC_ADDOWNER, 
                Arrays.<Type>asList(new Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> claimConnection(String serialNumber, String customerAddress) {
        final Function function = new Function(
                FUNC_CLAIMCONNECTION, 
                Arrays.<Type>asList(new Utf8String(serialNumber),
                new Address(160, customerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getOwners() {
        final Function function = new Function(FUNC_GETOWNERS, 
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
        final Function function = new Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isOwner(String addr) {
        final Function function = new Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(new Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeOwner(String owner) {
        final Function function = new Function(
                FUNC_REMOVEOWNER, 
                Arrays.<Type>asList(new Address(160, owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> retrieveConnection(String serialNumber) {
        final Function function = new Function(FUNC_RETRIEVECONNECTION, 
                Arrays.<Type>asList(new Utf8String(serialNumber)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeConnection(String serialNumber) {
        final Function function = new Function(
                FUNC_REVOKECONNECTION, 
                Arrays.<Type>asList(new Utf8String(serialNumber)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferConnection(String serialNumber, String customerAddress) {
        final Function function = new Function(
                FUNC_TRANSFERCONNECTION, 
                Arrays.<Type>asList(new Utf8String(serialNumber),
                new Address(160, customerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ConnectedPackagingContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ConnectedPackagingContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ConnectedPackagingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ConnectedPackagingContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ConnectedPackagingContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ConnectedPackagingContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ConnectedPackagingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ConnectedPackagingContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ConnectedPackagingContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ConnectedPackagingContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ConnectedPackagingContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ConnectedPackagingContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ConnectedPackagingContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ConnectedPackagingContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ConnectedPackagingContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ConnectedPackagingContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
