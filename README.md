# Bagging Library

## Description
This is a software library intended to support the creation, manipulation, and validation of "bags" from the [bagit specification](https://tools.ietf.org/html/draft-kunze-bagit). 
It currently supports version 0.93 through 1.0.

## Badges
|            |                                                    |
|------------|----------------------------------------------------|
|Build Status|[![CircleCI](https://img.shields.io/circleci/project/github/jscancella/bagging/master.svg?label=CircleCi&maxAge=600)](https://circleci.com/gh/jscancella/bagging)|
|Metrics|[![Coverage Status](https://coveralls.io/repos/github/jscancella/bagging/badge.svg?branch=master)](https://coveralls.io/github/jscancella/bagging?branch=master) [![Github Latest Release Downloads](https://img.shields.io/github/downloads/jscancella/bagging/latest/total.svg?maxAge=600)]()|
|Documentation|[![javadoc.io](https://img.shields.io/badge/javadoc.io-latest-blue.svg?maxAge=31556926)](http://www.javadoc.io/doc/gov.loc/bagit)|

## Requirements
* Java 8
* gradle (for development only)

## Documentation
We strive to have great documentation! Thus this file follows recommendations from https://www.divio.com/blog/documentation/
Editors and grammer aficionados are welcome and encouraged to edit this content to make it even better! See "Helping Contribute" below.

### Explanation
#### What is a "bag"?
A "bag" is a way to transfer files from one location to another and verify that the files sent are complete (you received exactly what you were supposed to receive), and that they are correct (none of the bits have changed). While many people use "bags" for other purposes, they are tangential to the purpose of file transfer. 

#### What is a typical use case for a bag?
Typically you need to send files out of band(i.e. not using the internet) and you need to ensure that all the files are received correctly. Usually this is due to the amount of files being transfered is very large, the internet connection is too slow or unreliable, or there is no physical connection to the internet.

#### What is fetching?
To save on transferring all the files (or multiple copies of the same file) you can use a fetch.txt file. This special file lists where those other files are located on the internet. This library does not handle trying to retrieve these files due to the complicated nature of retrieving files over the internet.

#### How do I include information about the bag within the bag? (metadata)
There is a special file called bag-info.txt (or package-info.txt for older versions) that is formatted for easy reading by humans. This file is just a list of key value pairs, and with some very few exceptions has no bearing on the bag other than to give additional information.

#### History of bagit
The BagIt specification was first created by the Library of Congress because it needed a way to verify that donated material on hard drives was correct and complete.

### How-to guides
#### how to read a bag
```java
Path rootDir = Paths.get("RootDirectoryOfExistingBag");
Bag bag = BagReader.read(rootDir);
```

#### how to write a bag
```java
Path outputDir = Paths.get("WhereYouWantToWriteTheBagTo");
BagWriter.write(bag, outputDir); //where bag is a Bag object
```

#### how to create a bag from a directory
```java
Path folder = Paths.get("FolderYouWantToBag");
boolean includeHiddenFiles = false;
Bag bag = BagWriter.bagInPlace(folder, Arrays.asList("sha512"), includeHiddenFiles);
```

#### how to validate a bag
There are three kinds of validations
1. Verify a bag is complete
2. Verify a bag is correct
3. Just check file count and bite size

##### Verify Complete
```java
boolean ignoreHiddenFiles = true;
BagVerifier.isComplete(bag, ignoreHiddenFiles);
```

##### Verify Valid (both complete and correct)
```java
boolean ignoreHiddenFiles = true;
BagVerifier.isValid(final Bag bag, final boolean ignoreHiddenFiles)
```

##### Quickly Verify (just check file count and bite size)
This may be removed in the future since it is mostly a hack of the bag metadata

```java
BagVerifier.quicklyVerify(bag); //where bag is a Bag object
```

#### How to lint a bag (check for potential issues)
```java
Path folder = Paths.get("BagYouWantToCheck");
Set<BagitWarning> warnings = BagLinter(folder);
```

#### How to verify against a profile
```java
Path rootDir = Paths.get("RootDirectoryOfExistingBag");
Bag bag = BagReader.read(rootDir);
InputStream jsonProfile = new URL("https://github.com/bagit-profiles/bagit-profiles/blob/1.1.0/bagProfileFoo.json").openStream();
assert BagLinter.checkAgainstProfile(jsonProfile, bag) == true;
```

#### How to include a new checksum algorithm
The [StandardHasher](https://github.com/jscancella/bagging/blob/master/src/main/java/com/github/jscancella/hash/StandardHasher.java) contains many well known checksum algorithm implementations. However, there will be times when you want(or must) use a different algorithm. The [BagitChecksumNameMapping](https://github.com/jscancella/bagging/blob/master/src/main/java/com/github/jscancella/hash/BagitChecksumNameMapping.java) contains the mapping between bagit checksum names and their implementation and is the only place you need to modify to change which implementation you would like to use.

```java
public enum SHA3Hasher implements Hasher {
  INSTANCE;// using enum to enforce singleton
  
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  private static final String MESSAGE_DIGEST_NAME = "SHA3-256";
  private MessageDigest messageDigestInstance;

  @Override
  public String hash(Path path) throws IOException{
    reset();
    updateMessageDigest(path, messageDigestInstance);
    return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void update(byte[] bytes, int length){
    messageDigestInstance.update(bytes, 0, length);
  }

  @Override
  public String getHash(){
    return formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void reset(){
    messageDigestInstance.reset();
  }

  @Override
  public String getBagitAlgorithmName(){
    return "sha3256";
  }
  
  private static void updateMessageDigest(final Path path, final MessageDigest messageDigest) throws IOException{
    try(final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = is.read(buffer);

      while(read != -1){
        messageDigest.update(buffer, 0, read);
        read = is.read(buffer);
      }
    }
  }
  
  private static String formatMessageDigest(final MessageDigest messageDigest){
    try(final Formatter formatter = new Formatter()){
      for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
      }
      
      return formatter.toString();
    }
  }

  @Override
  public void initialize() throws NoSuchAlgorithmException{
    messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
  }

}

BagitChecksumNameMapping.add("sha3256", SHA3Hasher.INSTANCE);
```

#### How to create a new conformance profile 
This is beyond the scope of this project, however please see https://github.com/bagit-profiles/bagit-profiles for in-depth documentation on profiles. 

### Technical Reference
One of the inspirations for writing this library was to create a simple to use interface for creating, reading, writing, verifying, and linting BagIt specification bags. The coding therefore tries to adhere with the best practices in [Effective Java by Joshua Bloch](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997) as well as experiences from the team members.

#### Classes/Methods not to use outside this project
There are many classes that were not designed to be used outside this project, the rules for this are
* If a class/method does not contain a javadoc
* If a class is in a package named "internal"
* If a class is final, that class was not designed to be extended by users outside this project

#### javadocs
All public interfaces/classes have javadocs detailing what the class's responsibilities are and, what the methods do and are used for. 
TODO - add link to http://www.javadoc.io/doc

#### code coverage
TODO - add link to https://coveralls.io

#### testing (conformance-suite)
Because there are many test cases for using the BagIt specification correctly, the Library of Congress decided to create a suite of known issues as well as cononical basic bags for each specification version. These test cases are stored in a git repository and can be found at https://github.com/libraryofcongress/bagit-conformance-suite.git
We use these test cases to ensure we are correctly adhering to the BagIt specification.  

## Frequently asked questions (FAQ)
#### BagIt, bag, bagit, Bagger, Bagging names are all so similar. Why?!
Naming stuff is hard, and generally we humans are bad at naming similar things distinctly. So in order to make it easier, here is a short list of definitions
* bag - a group of files in a particular format to ensure the files received are correct and complete
* BagIt - the name of the specification (or how is a bag structured)
* bagit (notice the all lowercase naming) - The original java implementation that also contained a command line utility by the same name.
* Bagger - a java desktop application (GUI) that used bagit to create bags
* Bagging - this library which tries to correctly implement all versions of the BagIt specification in a clean and concise way

#### is there a command line utility in here?
The short answer, no. The long answer is that having a java command line utility has caused more confusion and frustration than it has helped. If you need a command line implementation, try taking a look at the [python implementation from the Library of Congress](https://github.com/libraryofcongress/bagit-python) instead.

#### how do I include the tag-manifest checksum in the tag-manifest?
You don't because in order to generate a checksum of the tag file it can't change. Thus if you try to add a line for the tag-manifest containing a checksum, that checksum will change and no longer be valid.

#### how do I check/create a compressed (zip, gz, etc) bag using this library?
The specific functionality for checking or creating a bag while it is still compressed is not supported and there are not plans to support it in the future. I would recommend you use your favorite application to compress/decompress and then work with the bag as normal. 

#### is BagIt good for archiving?
Maybe, it really depends on what you are trying to achieve. 
Are you trying to create a super safe copy of your files that will prevent bitrot and other storage problems from ever happening? - then no the BagIt specification won't help you because it can't keep files safe (erasure codes, multiple copies, etc) but only aids in checking completeness and correctness. If that is your use case, I would recommend that you use the BagIt specification when you receive files outside the internet and then use other software to ensure those files are safe from corruption.

## Support
#### Bugs
If you find a bug in Bagging please let us know by [submitting a bug report!](https://github.com/jscancella/bagging/issues/new)
When creating a bug report please try to include the following information which will greatly aid in resolving the issue faster
* a small example showing the incorrect behavior
* the expected behavior
* the actual behavior
* the operating system being used
* version of Bagging being used

#### Feature requests
We would love to hear your ideas to make this library even better! First submit a new [issue](https://github.com/jscancella/bagging/issues/new) discussing what feature you would like added. Please include the following information when submitting
* Current behavior (if applicable)
* Proposed behavior
* Why this feature is useful
* A small code example if possible

#### Questions
It is impossible for this documentation to cover all questions that you might have. Therefore, if you don't understand something or would like more clarification please submit a [issue](https://github.com/jscancella/bagging/issues/new) with your question. I will try to answer it as best I can, and if useful will be added to the FAQ section.

#### Helping Contribute
If you value this project, please consider contributing! All pull requests will be reviewed with an aim to have them incorporated into this project. Don't know how to submit a pull request? No worries, check out github's great documentation at https://help.github.com/articles/about-pull-requests/
You will need to also sign a document stating that you freely give all copyright over to this project for any submitted pull requests.
Some of the items we will check when you submit a pull request are
* Does the pull request follow the style of the rest of the project?
* Do all the tests and other code quality checks still pass?
* Does the pull request maintain the same level of code coverage?
* If adding new functionality, were test cases added for the base case and several edge cases?
* Was documentation updated (if applicable)?

#### Other Languages
From the start Bagging was built knowing that not all people speak English. If you are able to translate from English to another language we would love your help! Please see the link for Transifex to get started.

#### Community
There is a very active community around digital archiving. One of which is The Digital Curation Google Group (https://groups.google.com/d/forum/digital-curation) which  is an open discussion list that reaches many of the contributors to and users of this project.

## Roadmap for this library
* Maintain interoperability with the [BagIt specification](https://tools.ietf.org/html/draft-kunze-bagit)
* Fix bugs/issues reported (on going)
* Translate to various languages (on going)
