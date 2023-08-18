package com.credenza3.credenzapassport.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
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
    public static final String BINARY = "0x60806040523480156200001157600080fd5b50620000266200002062000056565b6200008c565b6200003062000056565b600580546001600160a01b0319166001600160a01b039290921691909117905562000193565b6000601436108015906200007457506003546001600160a01b031633145b1562000087575060131936013560601c90565b503390565b60015415801590620000ae57503360009081526020819052604090205460ff16155b156200010b5760405162461bcd60e51b815260206004820152602260248201527f4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f604482015261371760f11b606482015260840160405180910390fd5b6001600160a01b03811660009081526020819052604090205460ff1615620001305750565b6001600160a01b03166000818152602081905260408120805460ff191660019081179091558054808201825591527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60180546001600160a01b0319169091179055565b611f0180620001a36000396000f3fe608060405234801561001057600080fd5b50600436106102275760003560e01c806386c58d3e11610130578063ce1b815f116100b8578063e1e610e11161007c578063e1e610e114610515578063e9858e3014610528578063ea0f40fe14610554578063ee28f50114610567578063f617d91d1461058757600080fd5b8063ce1b815f146104a2578063d4610483146104b3578063d4f1205c146104c6578063da742228146104d9578063dc79b176146104ec57600080fd5b8063954a85b4116100ff578063954a85b414610438578063a0e67e2b14610461578063a99752c414610469578063ad102b841461047c578063c75305bc1461048f57600080fd5b806386c58d3e1461040a5780638c0f9aac1461036557806390c3f38f1461041d57806391825dca1461043057600080fd5b806350ee7b69116101b3578063668ff42711610182578063668ff427146103915780637065cb48146103a4578063717fbc96146103b757806372dd52e3146103ca57806373e29b0d146103f557600080fd5b806350ee7b691461031d578063572b6c05146103305780635d0fd6c1146103525780635e9b45e11461036557600080fd5b806327a920dc116101fa57806327a920dc146102a057806329dd5120146102b35780632f54bf6e146102c657806334e0ee2e146102e9578063356baf0c1461030a57600080fd5b80630d8e6e2c1461022c57806315650cac14610270578063173825d9146102855780631a09254114610298575b600080fd5b60408051808201909152601681527504c6564676572436f6e7472616374207620312e302e360541b60208201525b6040516102679190611b9e565b60405180910390f35b61028361027e366004611905565b61059a565b005b610283610293366004611853565b610736565b61025a6107e0565b6102836102ae366004611853565b610872565b6102836102c1366004611937565b6108bb565b6102d96102d4366004611853565b6109f9565b6040519015158152602001610267565b6102fc6102f7366004611853565b610a17565b604051908152602001610267565b6102836103183660046118dc565b610a5e565b61028361032b3660046118a6565b610aa1565b6102d961033e366004611853565b6003546001600160a01b0391821691161490565b6102d9610360366004611853565b610ba4565b6102d9610373366004611853565b6001600160a01b03166000908152600a602052604090205460ff1690565b61028361039f366004611905565b610bd8565b6102836103b2366004611853565b610e00565b6102836103c5366004611ad6565b610ea8565b6004546103dd906001600160a01b031681565b6040516001600160a01b039091168152602001610267565b6103fd610efd565b6040516102679190611af8565b6102d9610418366004611874565b610f5e565b61028361042b366004611a31565b610fa5565b6103dd610fdf565b6103dd610446366004611abe565b6000908152600860205260409020546001600160a01b031690565b6103fd611018565b610283610477366004611853565b611078565b61028361048a3660046118dc565b611105565b61028361049d366004611853565b611204565b6003546001600160a01b03166103dd565b6102836104c1366004611853565b611256565b6102836104d4366004611853565b6112a1565b6102836104e7366004611853565b6112ea565b6102fc6104fa366004611853565b6001600160a01b031660009081526007602052604090205490565b6102836105233660046118dc565b61132f565b6102d9610536366004611853565b6001600160a01b031660009081526006602052604090205460ff1690565b6102fc610562366004611853565b6114db565b61057a610575366004611853565b611522565b6040516102679190611b45565b610283610595366004611853565b6115db565b6105a5610360611623565b6105ca5760405162461bcd60e51b81526004016105c190611c33565b60405180910390fd5b6001600160a01b03831660009081526006602052604090205460ff16156106035760405162461bcd60e51b81526004016105c190611cf9565b600082136106235760405162461bcd60e51b81526004016105c190611cb7565b61062f83610418611623565b1561064c5760405162461bcd60e51b81526004016105c190611c80565b6001600160a01b0383166000908152600c602052604081208054849290610674908490611d8a565b90915550506001600160a01b0383166000908152600d6020526040812080548492906106a1908490611d8a565b90915550506001600160a01b0383166000818152600e60209081526040808320815160608101835242815280840187815281840189815283546001818101865594885295872092516003909602909201948555519184019190915551600290920191909155518392859290917f67839115ecfc56e55723f233dbb40ca67429025c19e220e84a0de77357dce0fc9190a4505050565b6107416102d4611623565b61075d5760405162461bcd60e51b81526004016105c190611bf1565b610766816109f9565b6107b25760405162461bcd60e51b815260206004820152601e60248201527f5461726765742061646472657373206973206e6f7420616e206f776e6572000060448201526064016105c1565b6001600160a01b0381166000908152602081905260409020805460ff191690556107dd600182611657565b50565b6060600280546107ef90611e21565b80601f016020809104026020016040519081016040528092919081815260200182805461081b90611e21565b80156108685780601f1061083d57610100808354040283529160200191610868565b820191906000526020600020905b81548152906001019060200180831161084b57829003601f168201915b5050505050905090565b61087d610360611623565b6108995760405162461bcd60e51b81526004016105c190611c33565b600580546001600160a01b0319166001600160a01b0392909216919091179055565b6108c6610360611623565b6108e25760405162461bcd60e51b81526004016105c190611c33565b60005b81518110156109f5576006600083838151811061091257634e487b7160e01b600052603260045260246000fd5b602090810291909101810151516001600160a01b031682528101919091526040016000205460ff16156109575760405162461bcd60e51b81526004016105c190611cf9565b6109e382828151811061097a57634e487b7160e01b600052603260045260246000fd5b6020026020010151600001518383815181106109a657634e487b7160e01b600052603260045260246000fd5b6020026020010151602001518484815181106109d257634e487b7160e01b600052603260045260246000fd5b60200260200101516040015161059a565b806109ed81611e5c565b9150506108e5565b5050565b6001600160a01b031660009081526020819052604090205460ff1690565b6000610a2582610418611623565b15610a425760405162461bcd60e51b81526004016105c190611c80565b506001600160a01b03166000908152600c602052604090205490565b610a696102d4611623565b610a855760405162461bcd60e51b81526004016105c190611bf1565b6001600160a01b03909116600090815260076020526040902055565b610aaa826109f9565b15610af75760405162461bcd60e51b815260206004820152601860248201527f4f776e65722063616e206e6f7420626520626c6f636b6564000000000000000060448201526064016105c1565b816001600160a01b0316610b09611623565b6001600160a01b03161415610b575760405162461bcd60e51b815260206004820152601460248201527321b0b7303a10313637b1b5903cb7bab939b2b63360611b60448201526064016105c1565b8060096000610b64611623565b6001600160a01b0390811682526020808301939093526040918201600090812096909116815294909152909220805460ff19169215159290921790915550565b6000610baf826109f9565b80610bd257506001600160a01b0382166000908152600a602052604090205460ff165b92915050565b6001600160a01b03831660009081526006602052604090205460ff1615610c115760405162461bcd60e51b81526004016105c190611cf9565b610c19611623565b6001600160a01b0316836001600160a01b03161480610c3e5750610c3e610360611623565b610c8a5760405162461bcd60e51b815260206004820152601f60248201527f596f752063616e2072656465656d206f6e6c7920796f757220706f696e74730060448201526064016105c1565b610c9683610418611623565b15610cb35760405162461bcd60e51b81526004016105c190611c80565b60008213610cd35760405162461bcd60e51b81526004016105c190611cb7565b6001600160a01b0383166000908152600c6020526040902054821315610d2f5760405162461bcd60e51b81526020600482015260116024820152704e6f7420656e6f75676820706f696e747360781b60448201526064016105c1565b6001600160a01b0383166000908152600c602052604081208054849290610d57908490611dcb565b90915550506001600160a01b0383166000908152600e60209081526040918290208251606081018452428152918201849052918101610d9585611e77565b90528154600181810184556000938452602080852084516003909402019283558301519082015560409182015160029091015551829184916001600160a01b038716917fee3b1ba81f87423fecc5e13b69a00e5be4d7e9970fb24498122085bbef9b973891a4505050565b60015415801590610e175750610e15336109f9565b155b15610e345760405162461bcd60e51b81526004016105c190611bf1565b610e3d816109f9565b15610e455750565b6001600160a01b03166000818152602081905260408120805460ff191660019081179091558054808201825591527fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60180546001600160a01b0319169091179055565b610eb36102d4611623565b610ecf5760405162461bcd60e51b81526004016105c190611bf1565b60009182526008602052604090912080546001600160a01b0319166001600160a01b03909216919091179055565b6060600b80548060200260200160405190810160405280929190818152602001828054801561086857602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610f37575050505050905090565b6000610f69826109f9565b15610f7657506000610bd2565b506001600160a01b03918216600090815260096020908152604080832093909416825291909152205460ff1690565b610fb06102d4611623565b610fcc5760405162461bcd60e51b81526004016105c190611bf1565b80516109f590600290602084019061179e565b6000610fec610360611623565b6110085760405162461bcd60e51b81526004016105c190611c33565b506005546001600160a01b031690565b60606001805480602002602001604051908101604052809291908181526020018280548015610868576020028201919060005260206000209081546001600160a01b03168152600190910190602001808311610f37575050505050905090565b6110836102d4611623565b61109f5760405162461bcd60e51b81526004016105c190611bf1565b6001600160a01b03166000818152600a60205260408120805460ff19166001908117909155600b805491820181559091527f0175b7a638427703f0dbe7bb9bbf987a2551717b34e79f33b5b1008d1fa01db90180546001600160a01b0319169091179055565b6001600160a01b03821660009081526006602052604090205460ff161561113e5760405162461bcd60e51b81526004016105c190611cf9565b6000811361115e5760405162461bcd60e51b81526004016105c190611cb7565b61116a82826001610bd8565b600480546005546040516323b872dd60e01b81526001600160a01b0391821693810193909352848116602484015260448301849052169081906323b872dd90606401602060405180830381600087803b1580156111c657600080fd5b505af11580156111da573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906111fe9190611a15565b50505050565b61120f6102d4611623565b61122b5760405162461bcd60e51b81526004016105c190611bf1565b6001600160a01b0381166000908152600a60205260409020805460ff191690556107dd600b82611657565b611261610360611623565b61127d5760405162461bcd60e51b81526004016105c190611c33565b6001600160a01b03166000908152600660205260409020805460ff19166001179055565b6112ac610360611623565b6112c85760405162461bcd60e51b81526004016105c190611c33565b600480546001600160a01b0319166001600160a01b0392909216919091179055565b6112f56102d4611623565b6113115760405162461bcd60e51b81526004016105c190611bf1565b600380546001600160a01b0319166001600160a01b03831617905550565b61133a610360611623565b6113565760405162461bcd60e51b81526004016105c190611c33565b6001600160a01b03821660009081526006602052604090205460ff161561138f5760405162461bcd60e51b81526004016105c190611cf9565b600081136113af5760405162461bcd60e51b81526004016105c190611cb7565b6001600160a01b0382166000908152600c602052604090205481131561140b5760405162461bcd60e51b81526020600482015260116024820152704e6f7420656e6f75676820706f696e747360781b60448201526064016105c1565b6001600160a01b0382166000908152600c602052604081208054839290611433908490611dcb565b90915550506001600160a01b0382166000908152600d602052604081208054839290611460908490611dcb565b90915550506001600160a01b0382166000908152600e602090815260409182902082516060810184524281526002928101929092529181016114a184611e77565b9052815460018181018455600093845260209384902083516003909302019182559282015192810192909255604001516002909101555050565b60006114e982610418611623565b156115065760405162461bcd60e51b81526004016105c190611c80565b506001600160a01b03166000908152600d602052604090205490565b606061153082610418611623565b1561154d5760405162461bcd60e51b81526004016105c190611c80565b6001600160a01b0382166000908152600e6020908152604080832080548251818502810185019093528083529193909284015b828210156115d05783829060005260206000209060030201604051806060016040529081600082015481526020016001820154815260200160028201548152505081526020019060010190611580565b505050509050919050565b6115e6610360611623565b6116025760405162461bcd60e51b81526004016105c190611c33565b6001600160a01b03166000908152600660205260409020805460ff19169055565b60006014361080159061164057506003546001600160a01b031633145b15611652575060131936013560601c90565b503390565b60005b825481101561179957816001600160a01b031683828154811061168d57634e487b7160e01b600052603260045260246000fd5b6000918252602090912001546001600160a01b0316141561178757825483906116b890600190611e0a565b815481106116d657634e487b7160e01b600052603260045260246000fd5b9060005260206000200160009054906101000a90046001600160a01b031683828154811061171457634e487b7160e01b600052603260045260246000fd5b9060005260206000200160006101000a8154816001600160a01b0302191690836001600160a01b031602179055508280548061176057634e487b7160e01b600052603160045260246000fd5b600082815260209020810160001990810180546001600160a01b0319169055019055505050565b8061179181611e5c565b91505061165a565b505050565b8280546117aa90611e21565b90600052602060002090601f0160209004810192826117cc5760008555611812565b82601f106117e557805160ff1916838001178555611812565b82800160010185558215611812579182015b828111156118125782518255916020019190600101906117f7565b5061181e929150611822565b5090565b5b8082111561181e5760008155600101611823565b80356001600160a01b038116811461184e57600080fd5b919050565b600060208284031215611864578081fd5b61186d82611837565b9392505050565b60008060408385031215611886578081fd5b61188f83611837565b915061189d60208401611837565b90509250929050565b600080604083850312156118b8578182fd5b6118c183611837565b915060208301356118d181611ebd565b809150509250929050565b600080604083850312156118ee578182fd5b6118f783611837565b946020939093013593505050565b600080600060608486031215611919578081fd5b61192284611837565b95602085013595506040909401359392505050565b60006020808385031215611949578182fd5b823567ffffffffffffffff80821115611960578384fd5b818501915085601f830112611973578384fd5b81358181111561198557611985611ea7565b611993848260051b01611d59565b81815284810192508385016060808402860187018a10156119b2578788fd5b8795505b83861015611a075780828b0312156119cc578788fd5b6119d4611d30565b6119dd83611837565b815282880135888201526040808401359082015285526001959095019493860193908101906119b6565b509098975050505050505050565b600060208284031215611a26578081fd5b815161186d81611ebd565b60006020808385031215611a43578182fd5b823567ffffffffffffffff80821115611a5a578384fd5b818501915085601f830112611a6d578384fd5b813581811115611a7f57611a7f611ea7565b611a91601f8201601f19168501611d59565b91508082528684828501011115611aa6578485fd5b80848401858401378101909201929092529392505050565b600060208284031215611acf578081fd5b5035919050565b60008060408385031215611ae8578182fd5b8235915061189d60208401611837565b6020808252825182820181905260009190848201906040850190845b81811015611b395783516001600160a01b031683529284019291840191600101611b14565b50909695505050505050565b602080825282518282018190526000919060409081850190868401855b82811015611b915781518051855286810151878601528501518585015260609093019290850190600101611b62565b5091979650505050505050565b6000602080835283518082850152825b81811015611bca57858101830151858201604001528201611bae565b81811115611bdb5783604083870101525b50601f01601f1916929092016040019392505050565b60208082526022908201527f4f6e6c79206f776e65722063616e2063616c6c20746869732066756e6374696f604082015261371760f11b606082015260800190565b6020808252602d908201527f4f6e6c79206f776e6572206f7220706172746e65722063616e2063616c6c207460408201526c3434b990333ab731ba34b7b71760991b606082015260800190565b6020808252601e908201527f53656e646572206164647265737320626c6f636b656420627920757365720000604082015260600190565b60208082526022908201527f416d6f756e742073686f756c642062652067726561746572207468616e207a65604082015261726f60f01b606082015260800190565b6020808252601a908201527f546172676574206164647265737320776173206f7074206f7574000000000000604082015260600190565b6040516060810167ffffffffffffffff81118282101715611d5357611d53611ea7565b60405290565b604051601f8201601f1916810167ffffffffffffffff81118282101715611d8257611d82611ea7565b604052919050565b600080821280156001600160ff1b0384900385131615611dac57611dac611e91565b600160ff1b8390038412811615611dc557611dc5611e91565b50500190565b60008083128015600160ff1b850184121615611de957611de9611e91565b6001600160ff1b0384018313811615611e0457611e04611e91565b50500390565b600082821015611e1c57611e1c611e91565b500390565b600181811c90821680611e3557607f821691505b60208210811415611e5657634e487b7160e01b600052602260045260246000fd5b50919050565b6000600019821415611e7057611e70611e91565b5060010190565b6000600160ff1b821415611e8d57611e8d611e91565b0390565b634e487b7160e01b600052601160045260246000fd5b634e487b7160e01b600052604160045260246000fd5b80151581146107dd57600080fdfea2646970667358221220d2941b0f6864b79a790dfd3f1dd2dbd37379c766d36f8b902345bd43b87f1eaf64736f6c63430008040033";

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

    public static final String FUNC_GETDESCRIPTION = "getDescription";

    public static final String FUNC_GETGROUPMANAGER = "getGroupManager";

    public static final String FUNC_GETOWNERS = "getOwners";

    public static final String FUNC_GETPARTNERS = "getPartners";

    public static final String FUNC_GETTRUSTEDFORWARDER = "getTrustedForwarder";

    public static final String FUNC_GETUSERGROUP = "getUserGroup";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_ISAUTHORIZEDPARTNER = "isAuthorizedPartner";

    public static final String FUNC_ISBLOCKED = "isBlocked";

    public static final String FUNC_ISOPTOUT = "isOptOut";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_ISPARTNER = "isPartner";

    public static final String FUNC_ISPARTNEROROWNER = "isPartnerOrOwner";

    public static final String FUNC_ISTRUSTEDFORWARDER = "isTrustedForwarder";

    public static final String FUNC_OPTOUT = "optOut";

    public static final String FUNC_REDEEMPOINTS = "redeemPoints";

    public static final String FUNC_REMOVEAUTHORIZEDPARTNER = "removeAuthorizedPartner";

    public static final String FUNC_REMOVEOPTOUT = "removeOptOut";

    public static final String FUNC_REMOVEOWNER = "removeOwner";

    public static final String FUNC_RETRIEVELOGS = "retrieveLogs";

    public static final String FUNC_SETBLOCK = "setBlock";

    public static final String FUNC_SETCOINCONTRACTADDRESS = "setCoinContractAddress";

    public static final String FUNC_SETCOINTRANSFERSOURCEADDRESS = "setCoinTransferSourceAddress";

    public static final String FUNC_SETDESCRIPTION = "setDescription";

    public static final String FUNC_SETGROUPMANAGER = "setGroupManager";

    public static final String FUNC_SETTRUSTEDFORWARDER = "setTrustedForwarder";

    public static final String FUNC_SETUSERGROUP = "setUserGroup";

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
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADDPOINTS_EVENT, transactionReceipt);
        ArrayList<AddPointsEventResponse> responses = new ArrayList<AddPointsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDPOINTS_EVENT, log);
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
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REDEEMPOINTS_EVENT, transactionReceipt);
        ArrayList<RedeemPointsEventResponse> responses = new ArrayList<RedeemPointsEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
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
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REDEEMPOINTS_EVENT, log);
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
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, partnerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addBatchPoints(List<AddPointsBatchItem> items) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDBATCHPOINTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<AddPointsBatchItem>(AddPointsBatchItem.class, items)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addOwner(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addPoints(String recipient, BigInteger amount, BigInteger eventId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDPOINTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Int256(amount),
                        new org.web3j.abi.datatypes.generated.Uint256(eventId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> checkLifetimePoints(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKLIFETIMEPOINTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> checkPoints(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKPOINTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient)),
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
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Int256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> forfeitPoints(String recipient, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_FORFEITPOINTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Int256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getCoinTransferSourceAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCOINTRANSFERSOURCEADDRESS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getDescription() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETDESCRIPTION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getGroupManager(BigInteger groupId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETGROUPMANAGER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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

    public RemoteFunctionCall<List> getPartners() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPARTNERS,
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

    public RemoteFunctionCall<String> getTrustedForwarder() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETTRUSTEDFORWARDER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getUserGroup(String userAddr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETUSERGROUP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getVersion() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETVERSION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isAuthorizedPartner(String partnerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISAUTHORIZEDPARTNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, partnerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isBlocked(String userAddress, String partnerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISBLOCKED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddress),
                        new org.web3j.abi.datatypes.Address(160, partnerAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isOptOut(String userAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISOPTOUT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddress)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isOwner(String addr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, addr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPartner(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISPARTNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPartnerOrOwner(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISPARTNEROROWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isTrustedForwarder(String forwarder) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISTRUSTEDFORWARDER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, forwarder)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> optOut(String userAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_OPTOUT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> redeemPoints(String recipient, BigInteger amount, BigInteger eventId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REDEEMPOINTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Int256(amount),
                        new org.web3j.abi.datatypes.generated.Uint256(eventId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeAuthorizedPartner(String partnerAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEAUTHORIZEDPARTNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, partnerAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeOptOut(String userAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEOPTOUT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeOwner(String owner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REMOVEOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> retrieveLogs(String recipient) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RETRIEVELOGS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient)),
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

    public RemoteFunctionCall<TransactionReceipt> setBlock(String partnerAddress, Boolean isBlocked) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETBLOCK,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, partnerAddress),
                        new org.web3j.abi.datatypes.Bool(isBlocked)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCoinContractAddress(String coinContractAddress) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOINCONTRACTADDRESS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, coinContractAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setCoinTransferSourceAddress(String newAddr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOINTRANSFERSOURCEADDRESS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newAddr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setDescription(String description) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETDESCRIPTION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(description)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setGroupManager(BigInteger groupId, String manager) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETGROUPMANAGER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(groupId),
                        new org.web3j.abi.datatypes.Address(160, manager)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTrustedForwarder(String _forwarder) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETTRUSTEDFORWARDER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _forwarder)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setUserGroup(String userAddr, BigInteger groupId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETUSERGROUP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddr),
                        new org.web3j.abi.datatypes.generated.Uint256(groupId)),
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

    public static RemoteCall<LedgerContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LedgerContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<LedgerContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LedgerContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<LedgerContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LedgerContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<LedgerContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LedgerContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class AddPointsBatchItem extends StaticStruct {
        public String recipient;

        public BigInteger amount;

        public BigInteger eventId;

        public AddPointsBatchItem(String recipient, BigInteger amount, BigInteger eventId) {
            super(new org.web3j.abi.datatypes.Address(160, recipient),
                    new org.web3j.abi.datatypes.generated.Int256(amount),
                    new org.web3j.abi.datatypes.generated.Uint256(eventId));
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
            super(new org.web3j.abi.datatypes.generated.Uint256(timestamp),
                    new org.web3j.abi.datatypes.generated.Uint256(eventId),
                    new org.web3j.abi.datatypes.generated.Int256(pointsAmount));
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
