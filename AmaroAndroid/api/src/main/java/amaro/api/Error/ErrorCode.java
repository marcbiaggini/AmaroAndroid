package amaro.api.Error;

public enum ErrorCode {
  // Addresses API
  AddressNotFoundError,
  AmbiguousAddress,
  IncompleteAddress,
  // User APIs
  AlreadyValidatedError,
  InvalidKeyError,
  InvalidPinError,
  InvalidOldPasswordError,
  InvalidResetMethodError,
  UserAlreadyExistsError,
  UserNotFoundError,
  ValidationMethodError,
  VeryWeakPasswordError,
  InvalidShortPasswordError,
  ShortPasswordAlreadyInUse,
  WrongUsernamePasswordError,
  WrongConfirmationPassword,
  InvalidLedgerEntryType,
  InvalidLedgerEntryAmount,
  InvalidLedgerEntryDate,
  // Removing Owners
  SingleOwnerError,
  // Record Not found in database when searched by id
  RecordNotFoundException,
  FetchHolidayArgumentMissing,
  NotNullError,
  SmsDeliveryError,
  ForbiddenError,
  InternalServerError,
  InvalidArgumentError,
  MissingArgumentError,
  UnknownError,
  DeviceTypeDoesntSupportLocation,
  DeviceTypeDoesntSupportStations,
  DeleteAssignedRoleError,
  DuplicatedRoleError,
  LocationToDeleteContainsDeviceError,
  DestinationEqualsOrigin,

  InternalError,
  AlreadyRegisteredError,
  RegistrationError,

  // Clock-in / clock-out
  AlreadyClockedInError,
  AlreadyClockedOutError,
  AlreadyPausedError,
  AlreadyResumedError,
  InPauseError,
  NotPausedError,
  NotClockedInError,
  NotRunningError,
  InvalidClockSequence,
  ConnectionError,
  SSIDNotFoundError,
  PrintJobCannotBeDoneError,
  PrintJobInvalidPayloadError,
  AmaroServerError,
  NoHostSolved

}
