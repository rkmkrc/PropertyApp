package org.erkam.propertyuserservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingSaveRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateRequest;
import org.erkam.propertyuserservice.client.listing.dto.request.ListingUpdateStatusRequest;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingDeleteResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingSaveResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateResponse;
import org.erkam.propertyuserservice.client.listing.dto.response.ListingUpdateStatusResponse;
import org.erkam.propertyuserservice.client.listing.service.ListingService;
import org.erkam.propertyuserservice.client.payment.dto.request.PaymentRequest;
import org.erkam.propertyuserservice.client.payment.dto.response.PaymentResponse;
import org.erkam.propertyuserservice.client.payment.service.PaymentService;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.UserSuccessMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.dto.converter.product.PackageConverter;
import org.erkam.propertyuserservice.dto.converter.user.UserConverter;
import org.erkam.propertyuserservice.dto.request.auth.RegisterRequest;
import org.erkam.propertyuserservice.dto.request.user.BuyPackageRequest;
import org.erkam.propertyuserservice.dto.request.user.UserSaveRequest;
import org.erkam.propertyuserservice.dto.response.GenericResponse;
import org.erkam.propertyuserservice.dto.response.listing.ListingGetResponse;
import org.erkam.propertyuserservice.dto.response.product.PackageGetResponse;
import org.erkam.propertyuserservice.dto.response.product.BuyPackageResponse;
import org.erkam.propertyuserservice.dto.response.user.UserDeleteResponse;
import org.erkam.propertyuserservice.dto.response.user.UserGetResponse;
import org.erkam.propertyuserservice.dto.response.user.UserSaveResponse;
import org.erkam.propertyuserservice.exception.listing.ListingException;
import org.erkam.propertyuserservice.exception.listing.ListingExceptionMessage;
import org.erkam.propertyuserservice.exception.user.UserException;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.erkam.propertyuserservice.model.User;
import org.erkam.propertyuserservice.model.enums.ListingStatus;
import org.erkam.propertyuserservice.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ListingService listingService;
    private final PaymentService paymentService;

    // First check by email, to find out whether user exists or not,
    // if not exists then save if exists then throw an exception.
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail()));
            throw new UserException.UserAlreadyExistException(UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail());
        }
        User user = UserConverter.toUser(request);
        userRepository.save(user);
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_CREATED, request.getEmail()));
        return user;
    }

    // First check by email, to find out whether user exists or not,
    // if not exists then save if exists then throw an exception.
    public GenericResponse<UserSaveResponse> save(UserSaveRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail()));
            throw new UserException.UserAlreadyExistException(UserExceptionMessage.USER_ALREADY_EXISTS, request.getEmail());
        }
        userRepository.save(UserConverter.toUser(request));
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_CREATED, request.getEmail()));
        return GenericResponse.success(UserSaveResponse.of(request));
    }

    // Get all users from database if there is no data on database then throw an exception,
    // else convert users to UserGetResponse list then return it.
    public GenericResponse<List<UserGetResponse>> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.NO_DATA_ON_DATABASE));
            throw new UserException.NoDataOnDatabaseException(UserExceptionMessage.NO_DATA_ON_DATABASE);
        }
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.ALL_USERS_FETCHED));
        return GenericResponse.success(UserConverter.toUserGetResponseList(users));
    }

    // First check by id, to find out whether user exists or not,
    // if not exists then throw an exception, else return UserGetResponse
    public GenericResponse<UserGetResponse> getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_NOT_FOUND, id));
            return new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, id);
        });
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_FETCHED, user.getEmail()));
        return GenericResponse.success(UserConverter.toUserGetResponse(user));
    }

    // First check by email, to find out whether user exists or not,
    // if not exists then throw an exception, else return UserGetResponse
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_NOT_FOUND, email));
            return new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, email);
        });
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_FETCHED, user.getEmail()));
        return user;
    }

    // This method is implemented for disabling the direct interaction between
    // AppConfig with User Repository, instead of that AppConfig is interacting
    // on User Service
    public Optional<User> findByEmailForAppConfig(String username) {
        return userRepository.findByEmail(username);
    }

    // First check by id, to find out whether user exists or not,
    // if not exists then throw an exception, else delete the user and return UserDeleteResponse
    public GenericResponse<UserDeleteResponse> deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_NOT_FOUND, id));
            return new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, id);
        });
        userRepository.delete(user);
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_DELETED, user.getEmail()));
        return GenericResponse.success(UserDeleteResponse.of(user));
    }

    // First check is user authenticated, and has package
    // then request to listing service by feign client
    public GenericResponse<ListingGetResponse> addListing(ListingSaveRequest request) {

        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        // Check users eligibility to publish a listing
        if (!user.isUserEligibleToPublishListing()) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_ELIGIBLE_TO_PUBLISH_A_LISTING));
            throw new UserException.UserIsNotEligibleToAddListing(user.getEligibilityErrorMessages());
        }
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.USER_IS_ELIGIBLE_TO_PUBLISH_A_LISTING));

        // Request to Listing Service
        request.setUserId(user.getId());
        ListingGetResponse response = listingService.addListing(request);

        // Reduce the users quota by one
        user.reducePublishingQuotaByOne();

        // Save user
        userRepository.save(user);
        return GenericResponse.success(response);
    }

    // Check user is authenticated first,
    // then call payment service, if payment is successful
    // then assign package to the user.
    public GenericResponse<BuyPackageResponse> buyPackage(BuyPackageRequest request) {

        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        // Call payment service
        PaymentResponse response = paymentService.receivePayment(PaymentRequest.from(user, request));

        // Assign package to the user.
        user.assignPackage(request);

        // Update the user.
        user.updateUserAfterPurchasingAPackage(request);

        // Save user
        userRepository.save(user);

        return GenericResponse.success(BuyPackageResponse.of(request));
    }

    // Check user is authenticated first,
    // then return the packages of the user
    public GenericResponse<List<PackageGetResponse>> getAllPackages() {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        if (user.getPackages().size() == 0) {
            throw new UserException.UserHasNotAnyPackagesException(UserExceptionMessage.USER_HAS_NOT_ANY_PACKAGES, userEmail);
        }

        return GenericResponse.success(PackageConverter.toPackageGetResponseList(user.getPackages()));
    }

    // Check user is authenticated first,
    // then call listing service, then return the response.
    public GenericResponse<List<ListingGetResponse>> getAllListings() {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException.UserIsNotAuthenticatedException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        List<ListingGetResponse> response = listingService.getAllListingsOfUser(user.getId());

        if (response.size() == 0) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_HAS_NOT_ANY_LISTINGS));
            throw new UserException.UserHasNotAnyListingsException(UserExceptionMessage.USER_HAS_NOT_ANY_LISTINGS);
        }

        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.ALL_LISTINGS_OF_THE_USER_ARE_FETCHED, user.getEmail()));
        return GenericResponse.success(response);
    }

    // Check user is authenticated first,
    // then call listing service, then return the response.
    public GenericResponse<List<ListingGetResponse>> getPassiveListings() {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException.UserIsNotAuthenticatedException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        List<ListingGetResponse> response = listingService.getPassiveListingsOfUser(user.getId());

        if (response.size() == 0) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_HAS_NOT_ANY_PASSIVE_LISTINGS));
            throw new UserException.UserHasNotAnyPassiveListingsException(UserExceptionMessage.USER_HAS_NOT_ANY_PASSIVE_LISTINGS);
        }

        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.ALL_PASSIVE_LISTINGS_OF_THE_USER_ARE_FETCHED, user.getEmail()));
        return GenericResponse.success(response);
    }

    // Check user is authenticated first,
    // then call listing service, then return the response.
    public GenericResponse<List<ListingGetResponse>> getActiveListings() {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException.UserIsNotAuthenticatedException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        List<ListingGetResponse> response = listingService.getActiveListingsOfUser(user.getId());

        if (response.size() == 0) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_HAS_NOT_ANY_ACTIVE_LISTINGS));
            throw new UserException.UserHasNotAnyActiveListingsException(UserExceptionMessage.USER_HAS_NOT_ANY_ACTIVE_LISTINGS);
        }

        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.ALL_ACTIVE_LISTINGS_OF_THE_USER_ARE_FETCHED, user.getEmail()));
        return GenericResponse.success(response);
    }

    // Check user is authenticated first,
    // then call listing service, then return the response.
    public GenericResponse<ListingDeleteResponse> deleteListingById(Long id) {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException.UserIsNotAuthenticatedException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        ListingDeleteResponse response = listingService.deleteListing(id);

        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.LISTING_DELETED, id));
        return GenericResponse.success(response);
    }

    // Check user is authenticated first,
    // then call listing service, then return the response.
    public GenericResponse<ListingUpdateStatusResponse> updateTheStatusOfTheListing(ListingUpdateStatusRequest request) {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException.UserIsNotAuthenticatedException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        ListingUpdateStatusResponse response = listingService.updateTheStatusOfTheListing(request);
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.LISTING_STATUS_UPDATED, request.getStatus().name()));

        return GenericResponse.success(response);
    }

    // Check user is authenticated first,
    // then call listing service, then return the response.
    public GenericResponse<ListingGetResponse> updateTheListing(Long id, ListingUpdateRequest request) {
        // Check Authentication of the user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            log.error(LogMessage.generate(MessageStatus.NEG, UserExceptionMessage.USER_IS_NOT_AUTHENTICATED));
            throw new UserException.UserIsNotAuthenticatedException(UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);
        }

        // Get User
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException.UserNotFoundException(UserExceptionMessage.USER_NOT_FOUND, userEmail));

        // Check if the status is either ACTIVE or PASSIVE
        if (request.getStatus() != ListingStatus.ACTIVE && request.getStatus() != ListingStatus.PASSIVE) {
            throw new ListingException.InvalidTypeOfStatusException(ListingExceptionMessage.INVALID_TYPE_OF_STATUS, request.getStatus().name());
        }

        ListingGetResponse response = listingService.updateTheListing(id, request);
        log.info(LogMessage.generate(MessageStatus.POS, UserSuccessMessage.LISTING_UPDATED, request.getTitle()));

        return GenericResponse.success(response);
    }

    public GenericResponse<UserGetResponse> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println(userId);
        return getById(userId);
    }
}
