package io.github.jhipster.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.sample.domain.Operation;

import io.github.jhipster.sample.repository.OperationRepository;
import io.github.jhipster.sample.web.rest.util.HeaderUtil;
import io.github.jhipster.sample.web.rest.util.PaginationUtil;
import io.github.jhipster.sample.service.dto.OperationDTO;
import io.github.jhipster.sample.service.mapper.OperationMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Operation.
 */
@RestController
@RequestMapping("/api")
public class OperationResource {

    private final Logger log = LoggerFactory.getLogger(OperationResource.class);

    private static final String ENTITY_NAME = "operation";

    private final OperationRepository operationRepository;

    private final OperationMapper operationMapper;

    public OperationResource(OperationRepository operationRepository, OperationMapper operationMapper) {
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
    }

    /**
     * POST  /operations : Create a new operation.
     *
     * @param operationDTO the operationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new operationDTO, or with status 400 (Bad Request) if the operation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operations")
    @Timed
    public ResponseEntity<OperationDTO> createOperation(@Valid @RequestBody OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to save Operation : {}", operationDTO);
        if (operationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new operation cannot already have an ID")).body(null);
        }
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        OperationDTO result = operationMapper.toDto(operation);
        return ResponseEntity.created(new URI("/api/operations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /operations : Updates an existing operation.
     *
     * @param operationDTO the operationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated operationDTO,
     * or with status 400 (Bad Request) if the operationDTO is not valid,
     * or with status 500 (Internal Server Error) if the operationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/operations")
    @Timed
    public ResponseEntity<OperationDTO> updateOperation(@Valid @RequestBody OperationDTO operationDTO) throws URISyntaxException {
        log.debug("REST request to update Operation : {}", operationDTO);
        if (operationDTO.getId() == null) {
            return createOperation(operationDTO);
        }
        Operation operation = operationMapper.toEntity(operationDTO);
        operation = operationRepository.save(operation);
        OperationDTO result = operationMapper.toDto(operation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /operations : get all the operations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of operations in body
     */
    @GetMapping("/operations")
    @Timed
    public ResponseEntity<List<OperationDTO>> getAllOperations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Operations");
        Page<Operation> page = operationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operations");
        return new ResponseEntity<>(operationMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /operations/:id : get the "id" operation.
     *
     * @param id the id of the operationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the operationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/operations/{id}")
    @Timed
    public ResponseEntity<OperationDTO> getOperation(@PathVariable Long id) {
        log.debug("REST request to get Operation : {}", id);
        Operation operation = operationRepository.findOneWithEagerRelationships(id);
        OperationDTO operationDTO = operationMapper.toDto(operation);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(operationDTO));
    }

    /**
     * DELETE  /operations/:id : delete the "id" operation.
     *
     * @param id the id of the operationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/operations/{id}")
    @Timed
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        log.debug("REST request to delete Operation : {}", id);
        operationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
